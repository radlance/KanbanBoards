package com.github.radlance.board.create.data

import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.core.data.BoardEntity
import com.github.radlance.core.data.HandleError
import com.github.radlance.core.domain.BoardInfo
import com.github.radlance.invitation.data.InvitationEntity
import java.time.ZonedDateTime
import javax.inject.Inject

internal interface CreateBoardRemoteDataSource {

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
                    obj = BoardEntity(
                        name = name,
                        owner = myUser.id
                    )
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