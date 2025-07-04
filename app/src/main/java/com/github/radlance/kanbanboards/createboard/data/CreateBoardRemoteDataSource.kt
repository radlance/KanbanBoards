package com.github.radlance.kanbanboards.createboard.data

import com.github.radlance.kanbanboards.boards.data.BoardEntity
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CreateBoardRemoteDataSource {

    suspend fun createBoard(name: String)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : CreateBoardRemoteDataSource {

        override suspend fun createBoard(name: String) {
            val myUid = Firebase.auth.currentUser!!.uid
            val reference = provideDatabase.database().child("boards").push()
            reference.setValue(BoardEntity(name = name, owner = myUid)).await()
        }
    }
}