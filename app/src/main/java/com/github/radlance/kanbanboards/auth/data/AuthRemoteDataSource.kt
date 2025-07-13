package com.github.radlance.kanbanboards.auth.data

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRemoteDataSource {

    suspend fun signInWithToken(userTokenId: String)

    suspend fun signInWithEmail(email: String, password: String)

    class Base @Inject constructor(
        private val handle: HandleAuthRemoteDataSource
    ): AuthRemoteDataSource {

        override suspend fun signInWithToken(userTokenId: String) = handle.handle {
            Firebase.auth.signInWithCredential(
                GoogleAuthProvider.getCredential(userTokenId, null)
            ).await().user
        }

        override suspend fun signInWithEmail(email: String, password: String) {
            Firebase.auth.signInWithEmailAndPassword(email, password).await().user
        }
    }
}