package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.data.DataStoreManager
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import javax.inject.Inject

class RemoteProfileRepository @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val dataStoreManager: DataStoreManager,
    private val manageResource: ManageResource
) : ProfileRepository {

    override fun profile(): LoadProfileResult {
        val profile = remoteDataSource.profile()
        return LoadProfileResult.Base(
            name = profile.name ?: "",
            email = profile.email
        )
    }

    override suspend fun signOut() {
        dataStoreManager.saveAuthorized(authorized = false)
        return remoteDataSource.signOut()
    }

    override fun profileProvider(): com.github.radlance.api.service.ProfileProvider =
        remoteDataSource.profileProvider()

    override suspend fun deleteProfileWithGoogle(userTokenId: String): UnitResult {
        return try {
            remoteDataSource.deleteProfileWithGoogle(userTokenId)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(
                e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
            )
        }
    }

    override suspend fun deleteProfileWithEmail(email: String, password: String): UnitResult {
        return try {
            remoteDataSource.deleteProfileWithEmail(email, password)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(
                e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
            )
        }
    }
}