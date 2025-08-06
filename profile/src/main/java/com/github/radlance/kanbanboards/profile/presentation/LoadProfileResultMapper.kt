package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import javax.inject.Inject

internal class LoadProfileResultMapper @Inject constructor() :
    LoadProfileResult.Mapper<ProfileUiState> {

    override fun mapSuccess(name: String, email: String) = ProfileUiState.Base(name, email)

    override fun mapError(message: String) = ProfileUiState.Error(message)
}