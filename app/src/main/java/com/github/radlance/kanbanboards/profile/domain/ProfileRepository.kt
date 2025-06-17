package com.github.radlance.kanbanboards.profile.domain

interface ProfileRepository {

    fun profile(): LoadProfileResult

    suspend fun signOut()
}