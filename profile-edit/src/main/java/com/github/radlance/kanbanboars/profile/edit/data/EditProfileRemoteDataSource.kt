package com.github.radlance.kanbanboars.profile.edit.data

import com.github.radlance.kanbanboards.api.service.Auth
import com.github.radlance.kanbanboards.api.service.MyUser
import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.api.service.Service
import com.github.radlance.kanbanboards.core.data.HandleError
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal interface EditProfileRemoteDataSource {

    fun profile(): Flow<UserProfileEntity>

    fun profileProvider(): ProfileProvider

    fun editProfileName(name: String)

    suspend fun deleteProfileWithGoogle(userTokenId: String)

    suspend fun deleteProfileWithEmail(email: String, password: String)

    class Base @Inject constructor(
        private val service: Service,
        private val myUser: MyUser,
        private val auth: Auth,
        private val handleError: HandleError,
        private val handleEditProfileRemoteDataSource: HandleEditProfileRemoteDataSource
    ) : EditProfileRemoteDataSource {

        override fun profile(): Flow<UserProfileEntity> {
            return service.get(path = "users", subPath = myUser.id).mapNotNull {
                it.getValue(UserProfileEntity::class.java)
            }
        }

        override fun profileProvider(): ProfileProvider =
            myUser.profileProvider

        override fun editProfileName(name: String) {
            try {
                service.update(
                    path = "users",
                    subPath1 = myUser.id,
                    subPath2 = "name",
                    obj = name
                )
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        override suspend fun deleteProfileWithGoogle(userTokenId: String) {
            handleEditProfileRemoteDataSource.handleDelete {
                auth.deleteUser(userTokenId)
            }
        }

        override suspend fun deleteProfileWithEmail(email: String, password: String) {
            handleEditProfileRemoteDataSource.handleDelete {
                auth.deleteUser(email, password)
            }
        }
    }
}