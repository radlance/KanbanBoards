package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.api.service.MyUser
import com.github.radlance.kanbanboards.api.service.Service
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal interface ProfileRemoteDataSource {

    fun profile(): Flow<UserProfileEntity>

    fun signOut()

    class Base @Inject constructor(
        private val myUser: MyUser,
        private val service: Service
    ) : ProfileRemoteDataSource {

        override fun profile(): Flow<UserProfileEntity> {
            return service.get(path = "users", subPath = myUser.id).mapNotNull {
                it.getValue(UserProfileEntity::class.java)
            }
        }

        override fun signOut() {
            myUser.signOut()
        }
    }
}