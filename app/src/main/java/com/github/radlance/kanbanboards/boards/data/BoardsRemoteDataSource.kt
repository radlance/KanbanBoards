package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface BoardsRemoteDataSource {

    fun myBoard(): Flow<List<Board.Storage>>

    fun otherBoards(): Flow<List<Board.Storage>>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : BoardsRemoteDataSource {

        override fun myBoard(): Flow<List<Board.Storage>> {
            val myUserId = Firebase.auth.currentUser!!.uid
            val query = provideDatabase.database()
                .child("boards")
                .orderByChild("owner")
                .equalTo(myUserId)

            return query.snapshots.map<DataSnapshot, List<Board.Storage>> { snapshot ->
                snapshot.children.mapNotNull {
                    val key = it.key ?: return@mapNotNull null
                    val entity = it.getValue<BoardEntity>() ?: return@mapNotNull null
                    Board.My(key, entity.name)
                }

            }.catch { e -> throw IllegalStateException(e.message) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun otherBoards(): Flow<List<Board.Storage>> {
            val myUserId = Firebase.auth.currentUser!!.uid
            val membersQuery = provideDatabase.database()
                .child("boards-members")
                .orderByChild("memberId")
                .equalTo(myUserId)

            return membersQuery.snapshots.flatMapLatest { membersSnapshot ->
                val boardIds = membersSnapshot.children.mapNotNull {
                    it.getValue<OtherBoardEntity>()?.boardId
                }

                if (boardIds.isEmpty()) {
                    flowOf<List<Board.Storage>>(emptyList())
                } else {
                    combine(
                        boardIds.map { boardId ->
                            provideDatabase.database()
                                .child("boards")
                                .child(boardId)
                                .snapshots.mapNotNull { boardSnapshot ->
                                    boardSnapshot.getValue<BoardEntity>()?.let { entity ->
                                        Board.Other(
                                            id = boardSnapshot.key ?: "",
                                            name = entity.name,
                                            owner = entity.owner
                                        )
                                    }
                                }
                        }
                    ) { boards: Array<Board.Storage> -> boards.toList() }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }
    }
}