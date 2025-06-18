package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import javax.inject.Inject

class RemoteProfileRepository @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val dataStoreManager: DataStoreManager
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
}