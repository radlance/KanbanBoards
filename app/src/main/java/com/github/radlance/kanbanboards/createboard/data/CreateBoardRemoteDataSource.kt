package com.github.radlance.kanbanboards.createboard.data

import com.github.radlance.kanbanboards.board.data.BoardEntity
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CreateBoardRemoteDataSource {

    suspend fun createBoard(name: String): BoardInfo

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError
    ) : CreateBoardRemoteDataSource {

        override suspend fun createBoard(name: String): BoardInfo {
            return try {
                val myUid = Firebase.auth.currentUser!!.uid
                val reference = provideDatabase.database().child("boards").push()
                reference.setValue(BoardEntity(name = name, owner = myUid)).await()
                BoardInfo(id = reference.key!!, name = name, isMyBoard = true)
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }
    }
}