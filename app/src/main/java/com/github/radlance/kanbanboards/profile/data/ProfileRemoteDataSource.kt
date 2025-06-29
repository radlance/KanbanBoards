package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

interface ProfileRemoteDataSource {

    fun profile(): UserProfileEntity

    fun signOut()

    class Base @Inject constructor() : ProfileRemoteDataSource {

        override fun profile(): UserProfileEntity {
            val currentUser = Firebase.auth.currentUser!!
            return UserProfileEntity(currentUser.email!!, currentUser.displayName)
        }

        override fun signOut() {
            Firebase.auth.signOut()
        }
    }
}