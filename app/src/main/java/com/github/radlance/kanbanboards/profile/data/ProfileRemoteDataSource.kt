package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.ProfileProvider
import com.github.radlance.kanbanboards.service.Auth
import com.github.radlance.kanbanboards.service.MyUser
import javax.inject.Inject

interface ProfileRemoteDataSource {

    fun profile(): UserProfileEntity

    fun signOut()

    fun profileProvider(): ProfileProvider

    suspend fun deleteProfileWithGoogle(userTokenId: String)

    suspend fun deleteProfileWithEmail(email: String, password: String)

    class Base @Inject constructor(
        private val handleProfileRemoteDataSource: HandleProfileRemoteDataSource,
        private val myUser: MyUser,
        private val auth: Auth
    ) : ProfileRemoteDataSource {

        override fun profile(): UserProfileEntity = UserProfileEntity(
            myUser.email, myUser.displayName
        )

        override fun signOut() {
            myUser.signOut()
        }

        override fun profileProvider(): ProfileProvider = myUser.profileProvider

        override suspend fun deleteProfileWithGoogle(userTokenId: String) {
            handleProfileRemoteDataSource.handle {
                auth.deleteUser(userTokenId)
            }
        }

        override suspend fun deleteProfileWithEmail(email: String, password: String) {
            handleProfileRemoteDataSource.handle {
                auth.deleteUser(email, password)
            }
        }
    }
}