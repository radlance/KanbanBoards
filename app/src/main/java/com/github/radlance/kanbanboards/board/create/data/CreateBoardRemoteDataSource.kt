package com.github.radlance.kanbanboards.board.create.data

import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.invitation.data.InvitationEntity
import java.time.ZonedDateTime
import javax.inject.Inject

interface CreateBoardRemoteDataSource {

    fun createBoard(name: String, memberIds: List<String>): BoardInfo

    class Base @Inject constructor(
        private val service: Service,
        private val handleError: HandleError,
        private val myUser: MyUser
    ) : CreateBoardRemoteDataSource {

        override fun createBoard(name: String, memberIds: List<String>): BoardInfo {
            return try {
                val reference = service.post(
                    path = "boards",
                    obj = BoardEntity(name = name, owner = myUser.id)
                )

                val sendDate = ZonedDateTime.now()

                memberIds.forEach { memberId ->
                    service.post(
                        path = "boards-invitations",
                        obj = InvitationEntity(
                            memberId = memberId,
                            boardId = reference.id,
                            sendDate = sendDate.toString()
                        )
                    )
                }

                BoardInfo(id = reference.id, name = name, isMyBoard = true)
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }
    }
}