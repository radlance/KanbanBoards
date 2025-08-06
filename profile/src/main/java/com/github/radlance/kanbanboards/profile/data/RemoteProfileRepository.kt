package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.core.data.DataStoreManager
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RemoteProfileRepository @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val dataStoreManager: DataStoreManager
) : ProfileRepository {

    override fun profile(): Flow<LoadProfileResult> {
        return remoteDataSource.profile().map<UserProfileEntity, LoadProfileResult> {
            LoadProfileResult.Success(it.name ?: "", it.email)
        }.catch { e -> emit(LoadProfileResult.Error(e.message!!)) }
    }

    override suspend fun signOut() {
        dataStoreManager.saveAuthorized(authorized = false)
        return remoteDataSource.signOut()
    }
}