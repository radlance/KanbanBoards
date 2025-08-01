package com.github.radlance.kanbanboards.service

import com.github.radlance.kanbanboards.profile.domain.ProfileProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

interface MyUser {

    val id: String

    val email: String

    val displayName: String

    val profileProvider: ProfileProvider

    val exists: Boolean

    fun signOut()

    class Base : MyUser {

        private val currentUser get() = Firebase.auth.currentUser!!

        override val id: String get() = currentUser.uid

        override val email: String get() = currentUser.email!!

        override val displayName: String get() = currentUser.displayName!!

        override val profileProvider: ProfileProvider get() = when (
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
    }
}