package com.github.radlance.kanbanboards.common.data

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RemoteDataSource {

    suspend fun signIn(userTokenId: String)

    fun userExists(): Boolean

    class FirebaseClient @Inject constructor(
        private val handle: HandleError,
        private val provideDatabase: ProvideDatabase
    ) : RemoteDataSource {

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

        override fun userExists(): Boolean = Firebase.auth.currentUser != null
    }
}

private data class UserProfileEntity(
    val email: String,
    val name: String?
)