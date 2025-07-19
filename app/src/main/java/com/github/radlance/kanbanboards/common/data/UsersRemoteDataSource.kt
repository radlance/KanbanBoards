package com.github.radlance.kanbanboards.common.data

import com.github.radlance.kanbanboards.common.domain.User
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface UsersRemoteDataSource {

    fun user(userId: String): Flow<User>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : UsersRemoteDataSource {

        override fun user(userId: String): Flow<User> = provideDatabase
            .database()
            .child("users")
            .child(userId)
            .snapshots.mapNotNull { memberSnapshot ->
                val userProfileEntity = memberSnapshot.getValue<UserProfileEntity>()
                with(userProfileEntity ?: return@mapNotNull null) {
                    User(id = userId, email = email, name = name ?: "")
                }
            }
    }
}