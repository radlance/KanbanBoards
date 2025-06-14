package com.github.radlance.kanbanboards.navigation.domain

import kotlinx.coroutines.flow.Flow

interface NavigationRepository {

    fun authorizedStatus(): Flow<Boolean>
}