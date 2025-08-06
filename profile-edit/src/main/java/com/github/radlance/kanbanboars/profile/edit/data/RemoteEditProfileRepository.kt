package com.github.radlance.kanbanboars.profile.edit.data

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.core.data.HandleUnitResult
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboars.profile.edit.domain.EditProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RemoteEditProfileRepository @Inject constructor(
    private val remoteDataSource: EditProfileRemoteDataSource,
    private val handleUnitResult: HandleUnitResult
) : EditProfileRepository {

    override fun profile(): Flow<LoadProfileResult> {
        return remoteDataSource.profile().map<UserProfileEntity, LoadProfileResult> {
            LoadProfileResult.Success(it.name ?: "", it.email)
        }.catch { e -> emit(LoadProfileResult.Error(e.message!!)) }
    }

    override fun profileProvider(): ProfileProvider = remoteDataSource.profileProvider()

    override fun editProfile(name: String) = handleUnitResult.handle {
        remoteDataSource.editProfileName(name)
    }

    override suspend fun deleteProfile(userTokenId: String) = handleUnitResult.handleSuspend {
        remoteDataSource.deleteProfileWithGoogle(userTokenId)
    }

    override suspend fun deleteProfile(email: String, password: String) =
        handleUnitResult.handleSuspend {
            remoteDataSource.deleteProfileWithEmail(email, password)
        }
}