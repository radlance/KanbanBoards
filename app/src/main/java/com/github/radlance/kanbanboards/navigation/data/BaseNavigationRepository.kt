package com.github.radlance.kanbanboards.navigation.data

import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseNavigationRepository @Inject constructor(
    private val remoteDataSource: NavigationRemoteDataSource,
    private val dataStoreManager: DataStoreManager
) : NavigationRepository {

    override fun authorizedStatus(): Flow<Boolean> {
        val userExists = remoteDataSource.userExists()
        return dataStoreManager.authorized().map { authorized -> authorized && userExists }
    }
}