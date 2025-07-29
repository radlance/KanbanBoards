package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import javax.inject.Inject

class DeleteProfileMapper @Inject constructor() : UnitResult.Mapper<DeleteProfileUiState> {

    override fun mapSuccess() = DeleteProfileUiState.Success

    override fun mapError(message: String) = DeleteProfileUiState.Error(message)
}