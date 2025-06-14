package com.github.radlance.kanbanboards.auth.presentation

import com.github.radlance.kanbanboards.auth.domain.AuthResult
import javax.inject.Inject

class SignInResultMapper @Inject constructor(): AuthResult.Mapper<SignInResultUiState> {

    override fun mapSuccess(): SignInResultUiState = SignInResultUiState.Success

    override fun mapError(message: String): SignInResultUiState = SignInResultUiState.Error(message)
}