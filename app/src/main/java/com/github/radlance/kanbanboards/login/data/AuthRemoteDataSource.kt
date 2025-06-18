package com.github.radlance.kanbanboards.login.data

import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRemoteDataSource {

    suspend fun signIn(userTokenId: String)

    class Base @Inject constructor(
        private val handle: HandleError,
        private val provideDatabase: ProvideDatabase
    ): AuthRemoteDataSource {

        override suspend fun signIn(userTokenId: String) {
            try {
                val user = Firebase.auth.signInWithCredential(
                    GoogleAuthProvider.getCredential(userTokenId, null)
                ).await().user

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