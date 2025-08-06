package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.core.domain.UnitResult
import javax.inject.Inject

internal class EditProfileMapper @Inject constructor() : UnitResult.Mapper<EditProfileUiState> {

    override fun mapSuccess(): EditProfileUiState = EditProfileUiState.Success

    override fun mapError(message: String): EditProfileUiState = EditProfileUiState.Error(message)
}