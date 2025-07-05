package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.boards.data.BoardEntity
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface BoardRemoteDataSource {

    fun loadBoard(boardId: String): Flow<BoardInfo>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : BoardRemoteDataSource {

        override fun loadBoard(boardId: String): Flow<BoardInfo> {
            val myUserId = Firebase.auth.currentUser!!.uid

            return provideDatabase.database()
                .child("boards")
                .child(boardId).snapshots.mapNotNull {
                    val key = it.key ?: return@mapNotNull null
                    val entity = it.getValue<BoardEntity>() ?: return@mapNotNull null
                    BoardInfo(id = key, name = entity.name, isMyBoard = myUserId == entity.owner)
                }
        }
    }
}