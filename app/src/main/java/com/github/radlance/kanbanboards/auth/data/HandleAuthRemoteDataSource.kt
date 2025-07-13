package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface HandleAuthRemoteDataSource {

    suspend fun handle(action: suspend () -> FirebaseUser?)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handle: HandleError,
    ): HandleAuthRemoteDataSource {
        override suspend fun handle(action: suspend () -> FirebaseUser?) {
            try {
                val user = action.invoke()
                val uid = user!!.uid
                val email = user.email!!
                val name = user.displayName

                provideDatabase.database()
                    .child("users")
                    .child(uid)
                    .setValue(UserProfileEntity(email, name))
                    .await()

            } catch (e: Exception) {
                throw handle.handle(e)
            }
        }
    }
}