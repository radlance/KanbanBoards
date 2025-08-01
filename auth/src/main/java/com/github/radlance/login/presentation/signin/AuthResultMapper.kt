package com.github.radlance.login.presentation.signin

import com.github.radlance.common.domain.UnitResult
import javax.inject.Inject

internal class AuthResultMapper @Inject constructor() : UnitResult.Mapper<AuthResultUiState> {

    override fun mapSuccess(): AuthResultUiState = AuthResultUiState.Success

    override fun mapError(message: String): AuthResultUiState = AuthResultUiState.Error(message)
}