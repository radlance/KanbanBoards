package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import javax.inject.Inject

interface ProfileMapperFacade {

    fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState
}

internal class BaseProfileMapperFacade @Inject constructor(
    private val loadProfileMapper: LoadProfileResult.Mapper<ProfileUiState>
) : ProfileMapperFacade {

    override fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState {
        return loadProfileResult.map(loadProfileMapper)
    }
}