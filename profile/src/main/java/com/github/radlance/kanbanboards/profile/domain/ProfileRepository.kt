package com.github.radlance.kanbanboards.profile.domain

import kotlinx.coroutines.flow.Flow

interface ProfileInfoRepository {

    fun profile(): Flow<LoadProfileResult>
}

interface ProfileRepository : ProfileInfoRepository {

    suspend fun signOut()
}