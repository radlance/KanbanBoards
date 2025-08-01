package com.github.radlance.api.service

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Auth {

    suspend fun signIn(userTokenId: String): NewMyUser

    suspend fun signIn(email: String, password: String): NewMyUser

    suspend fun signUp(name: String, email: String, password: String): NewMyUser

    fun deleteUser(userTokenId: String)

    fun deleteUser(email: String, password: String)
}

internal abstract class AbstractAuth : Auth {

    protected fun newMyUser(firebaseUser: FirebaseUser?): NewMyUser {
        return with(firebaseUser!!) { NewMyUser(uid, email!!, displayName!!) }
    }

    protected fun deleteUserWithCredential(credential: AuthCredential) {
        val currentUser = Firebase.auth.currentUser!!
        currentUser.reauthenticate(credential)
        currentUser.delete()
    }
}

internal class BaseAuth @Inject constructor() : AbstractAuth() {
    override suspend fun signIn(userTokenId: String): NewMyUser {
        val user = Firebase.auth.signInWithCredential(
            GoogleAuthProvider.getCredential(userTokenId, null)
        ).await().user

        return newMyUser(user)
    }

    override suspend fun signIn(email: String, password: String): NewMyUser {
        val user = Firebase.auth.signInWithEmailAndPassword(email, password).await().user

        return newMyUser(user)
    }

    override suspend fun signUp(name: String, email: String, password: String): NewMyUser {
        val user = Firebase.auth.createUserWithEmailAndPassword(email, password).await().user

        user!!.updateProfile(
            UserProfileChangeRequest.Builder().apply {
                displayName = name
            }.build()
        ).await()

        return newMyUser(user)
    }

    override fun deleteUser(userTokenId: String) {

        val credential = GoogleAuthProvider.getCredential(userTokenId, null)
        deleteUserWithCredential(credential)
    }

    override fun deleteUser(email: String, password: String) {

        val credential = EmailAuthProvider.getCredential(email, password)
        deleteUserWithCredential(credential)
    }
}