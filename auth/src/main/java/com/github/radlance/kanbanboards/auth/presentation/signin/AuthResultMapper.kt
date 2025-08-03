package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.core.domain.UnitResult
import javax.inject.Inject

internal class AuthResultMapper @Inject constructor() : UnitResult.Mapper<AuthResultUiState> {

    override fun mapSuccess(): AuthResultUiState = AuthResultUiState.Success

    override fun mapError(message: String): AuthResultUiState = AuthResultUiState.Error(message)
}