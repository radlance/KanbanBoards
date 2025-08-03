package com.github.radlance.profile.presentation

import com.github.radlance.core.domain.UnitResult
import javax.inject.Inject

internal class DeleteProfileMapper @Inject constructor() : UnitResult.Mapper<DeleteProfileUiState> {

    override fun mapSuccess() = DeleteProfileUiState.Success

    override fun mapError(message: String) = DeleteProfileUiState.Error(message)
}