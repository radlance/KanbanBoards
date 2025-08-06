package com.github.radlance.kanbanboars.profile.edit.domain

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.ProfileInfoRepository

interface EditProfileRepository : ProfileInfoRepository {

    fun profileProvider(): ProfileProvider

    fun editProfile(name: String): UnitResult

    suspend fun deleteProfile(userTokenId: String): UnitResult

    suspend fun deleteProfile(email: String, password: String): UnitResult
}