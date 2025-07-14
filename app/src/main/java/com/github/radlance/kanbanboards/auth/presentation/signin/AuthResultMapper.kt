package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.domain.AuthResult
import javax.inject.Inject

class AuthResultMapper @Inject constructor() : AuthResult.Mapper<AuthResultUiState> {

    override fun mapSuccess(): AuthResultUiState = AuthResultUiState.Success

    override fun mapError(message: String): AuthResultUiState = AuthResultUiState.Error(message)
}