package com.github.radlance.kanbanboards.board.create.data

import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.invitation.data.InvitationEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.time.ZonedDateTime
import javax.inject.Inject

interface CreateBoardRemoteDataSource {

    suspend fun createBoard(name: String, memberIds: List<String>): BoardInfo

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError
    ) : CreateBoardRemoteDataSource {

        override suspend fun createBoard(name: String, memberIds: List<String>): BoardInfo {
            return try {
                val myUid = Firebase.auth.currentUser!!.uid
                val boardsReference = provideDatabase.database().child("boards").push()
                boardsReference.setValue(BoardEntity(name = name, owner = myUid)).await()

                val sendDate = ZonedDateTime.now()

                memberIds.forEach { memberId ->
                    provideDatabase.database()
                        .child("boards-invitations")
                        .push()
                        .setValue(
                            InvitationEntity(
                                memberId = memberId,
                                boardId = boardsReference.key!!,
                                sendDate = sendDate.toString()
                            )
                        )
                        .await()
                }

                BoardInfo(id = boardsReference.key!!, name = name, isMyBoard = true)
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }
    }
}