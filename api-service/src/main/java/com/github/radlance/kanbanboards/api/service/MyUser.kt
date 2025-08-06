package com.github.radlance.kanbanboards.api.service

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import javax.inject.Inject

interface MyUser {

    val id: String

    val email: String

    val displayName: String

    val profileProvider: ProfileProvider

    val exists: Boolean

    fun signOut()

    fun updateDisplayedName(name: String)
}

internal class BaseMyUser @Inject constructor() : MyUser {

    private val currentUser get() = Firebase.auth.currentUser!!

    override val id: String get() = currentUser.uid

    override val email: String get() = currentUser.email!!

    override val displayName: String get() = currentUser.displayName!!

    override val profileProvider: ProfileProvider
        get() = when (
            currentUser.providerData[1].providerId
        ) {
            GoogleAuthProvider.PROVIDER_ID -> ProfileProvider.Google
            EmailAuthProvider.PROVIDER_ID -> ProfileProvider.Email
            else -> throw IllegalStateException("unknown provider type")
        }

    override val exists: Boolean get() = Firebase.auth.currentUser != null

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override fun updateDisplayedName(name: String) {
        val currentUser = Firebase.auth.currentUser!!
        currentUser.updateProfile(
            UserProfileChangeRequest.Builder().apply {
                displayName = name
            }.build()
        )
    }
}