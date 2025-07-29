package com.github.radlance.kanbanboards.profile.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult

interface ProfileRepository {

    fun profile(): LoadProfileResult

    suspend fun signOut()

    fun profileProvider(): ProfileProvider

    suspend fun deleteProfileWithGoogle(userTokenId: String): UnitResult

    suspend fun deleteProfileWithEmail(email: String, password: String): UnitResult
}