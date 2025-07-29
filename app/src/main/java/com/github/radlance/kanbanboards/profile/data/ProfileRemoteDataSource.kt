package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.ProfileProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import javax.inject.Inject

interface ProfileRemoteDataSource {

    fun profile(): UserProfileEntity

    fun signOut()

    fun profileProvider(): ProfileProvider

    class Base @Inject constructor() : ProfileRemoteDataSource {

        override fun profile(): UserProfileEntity {
            val currentUser = Firebase.auth.currentUser!!
            return UserProfileEntity(currentUser.email!!, currentUser.displayName)
        }

        override fun signOut() {
            Firebase.auth.signOut()
        }

        override fun profileProvider(): ProfileProvider {
            val currentUser = Firebase.auth.currentUser!!

            return when (currentUser.providerData[1].providerId) {
                GoogleAuthProvider.PROVIDER_ID -> ProfileProvider.Google
                EmailAuthProvider.PROVIDER_ID -> ProfileProvider.Email
                else -> throw IllegalStateException("unknown provider type")
            }
        }
    }
}