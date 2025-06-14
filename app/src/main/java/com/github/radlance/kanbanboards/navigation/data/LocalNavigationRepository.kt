package com.github.radlance.kanbanboards.navigation.data

import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalNavigationRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
): NavigationRepository {

    override fun authorizedStatus(): Flow<Boolean> = dataStoreManager.authorized()
}