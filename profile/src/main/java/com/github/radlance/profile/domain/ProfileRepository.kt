package com.github.radlance.profile.domain

import com.github.radlance.core.domain.UnitResult

interface ProfileRepository {

    fun profile(): LoadProfileResult

    suspend fun signOut()

    fun profileProvider(): com.github.radlance.api.service.ProfileProvider

    suspend fun deleteProfileWithGoogle(userTokenId: String): UnitResult

    suspend fun deleteProfileWithEmail(email: String, password: String): UnitResult
}