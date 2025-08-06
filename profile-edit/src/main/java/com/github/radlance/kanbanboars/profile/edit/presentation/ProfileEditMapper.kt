package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import javax.inject.Inject

internal class ProfileEditMapper @Inject constructor() :
    LoadProfileResult.Mapper<ProfileEditUiState> {

    override fun mapSuccess(name: String, email: String) = ProfileEditUiState.Base(
        name = name, email = email
    )

    override fun mapError(message: String) = ProfileEditUiState.Error(message)
}