package com.github.radlance.kanbanboards.board.core.data

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface BoardRemoteDataSource {

    fun board(boardId: String): Flow<BoardInfo?>

    suspend fun leaveBoard(boardId: String)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : BoardRemoteDataSource {

        override fun board(boardId: String): Flow<BoardInfo?> {
            val myUserId = Firebase.auth.currentUser!!.uid

            return combine(
                provideDatabase.database()
                    .child("boards")
                    .child(boardId)
                    .snapshots,
                provideDatabase.database()
                    .child("boards-members")
                    .orderByChild("boardId")
                    .equalTo(boardId)
                    .snapshots
            ) { boardSnapshot, membersSnapshot ->
                val key = boardSnapshot.key ?: return@combine null
                val entity = boardSnapshot.getValue<BoardEntity>() ?: return@combine null

                val isMember = membersSnapshot.children.any {
                    it.getValue<BoardMemberEntity>()?.memberId == myUserId
                }

                if (entity.owner != myUserId && !isMember) {
                    return@combine null
                }

                BoardInfo(
                    id = key,
                    name = entity.name,
                    isMyBoard = myUserId == entity.owner,
                    owner = entity.owner
                )
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override suspend fun leaveBoard(boardId: String) {
            try {
                val myUserId = Firebase.auth.currentUser!!.uid
                val boardMemberSnapshot = provideDatabase.database()
                    .child("boards-members")
                    .orderByChild("boardId")
                    .equalTo(boardId)
                    .get().await()

                val result = boardMemberSnapshot.children.firstOrNull {
                    it.child("memberId").getValue<String>() == myUserId
                }

                result?.ref?.removeValue()?.await()
            } catch (_: Exception) {
            }
        }
    }
}