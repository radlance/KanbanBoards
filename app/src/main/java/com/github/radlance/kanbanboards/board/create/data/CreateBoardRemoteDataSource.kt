package com.github.radlance.kanbanboards.board.create.data

import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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
                val membersReference = provideDatabase.database().child("boards-members").push()
                memberIds.forEach { memberId ->
                    membersReference.setValue(
                        BoardMemberEntity(memberId = memberId, boardId = boardsReference.key!!)
                    ).await()
                }
                BoardInfo(id = boardsReference.key!!, name = name, isMyBoard = true)
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }
    }
}