package com.github.radlance.kanbanboards.common.data

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

interface RemoteDataSource {

    suspend fun signIn(userTokenId: String)

    class FirebaseClient @Inject constructor(
        private val firebase: Firebase,
        private val handle: HandleError
    ) : RemoteDataSource {

        override suspend fun signIn(userTokenId: String) {
            try {
                firebase.auth.signInWithCredential(
                    GoogleAuthProvider.getCredential(userTokenId, null)
                )
            } catch (e: Exception) {
                throw handle.handle(e)
            }
        }
    }
}