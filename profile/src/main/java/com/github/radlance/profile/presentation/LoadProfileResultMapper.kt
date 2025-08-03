package com.github.radlance.profile.presentation

import com.github.radlance.profile.domain.LoadProfileResult
import javax.inject.Inject

internal class LoadProfileResultMapper @Inject constructor() :
    LoadProfileResult.Mapper<ProfileUiState> {

    override fun map(name: String, email: String): ProfileUiState = ProfileUiState.Base(name, email)
}