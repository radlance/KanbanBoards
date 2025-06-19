package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface BoardsRemoteDataSource {

    fun myBoard(): Flow<List<Board>>

    fun otherBoards(): Flow<List<Board>>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : BoardsRemoteDataSource {

        override fun myBoard(): Flow<List<Board>> {
            val myUserId = Firebase.auth.currentUser!!.uid
            val query = provideDatabase.database()
                .child("boards")
                .orderByChild("owner")
                .equalTo(myUserId)

            return query.snapshots
                .map { snapshot ->
                    snapshot.children.mapNotNull {
                        val key = it.key ?: return@mapNotNull null
                        val entity = it.getValue<BoardEntity>() ?: return@mapNotNull null
                        Board.My(key, entity.name)
                    }
                }
                .catch { e -> throw IllegalStateException(e.message) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun otherBoards(): Flow<List<Board>> {
            val myUserId = Firebase.auth.currentUser!!.uid
            val membersQuery = provideDatabase.database()
                .child("boards-members")
                .orderByChild("memberId")
                .equalTo(myUserId)

            return membersQuery.snapshots
                .flatMapLatest { membersSnapshot ->
                    val boardIds = membersSnapshot.children.mapNotNull {
                        it.getValue<OtherBoardEntity>()?.boardId
                    }

                    if (boardIds.isEmpty()) {
                        flow { emit(emptyList()) }
                    } else {
                        combine(
                            boardIds.map { boardId ->
                                provideDatabase.database()
                                    .child("boards")
                                    .child(boardId)
                                    .snapshots
                                    .map { boardSnapshot ->
                                        boardSnapshot.getValue<BoardEntity>()?.let { entity ->
                                            Board.Other(
                                                id = boardSnapshot.key ?: "",
                                                name = entity.name,
                                                owner = entity.owner
                                            )
                                        }
                                    }
                            }
                        ) { boards -> boards.filterNotNull() }
                    }
                }
                .catch { e -> throw IllegalStateException(e.message) }
        }
    }
}

private data class BoardEntity(
    val name: String = "",
    val owner: String = ""
)

private data class OtherBoardEntity(
    val memberId: String = "",
    val boardId: String  = ""
)