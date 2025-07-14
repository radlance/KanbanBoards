package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.presentation.common.BaseAuthViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync

abstract class BaseSignInViewModel(
    private val authMapper: AuthResult.Mapper<AuthResultUiState>,
    protected val handleSignIn: HandleSignIn,
    runAsync: RunAsync
) : BaseAuthViewModel(handleSignIn, runAsync) {

    protected fun handleAuth(action: suspend () -> AuthResult) {
        handleSignIn.saveCredentialState(CredentialUiState.Initial)
        handleSignIn.saveAuthState(AuthResultUiState.Loading)

        handle(background = action) { result ->
            handleSignIn.saveAuthState(result.map(authMapper))
        }
    }
}