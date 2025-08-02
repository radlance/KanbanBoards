package com.github.radlance.auth.presentation.signin

import com.github.radlance.auth.presentation.common.BaseAuthViewModel
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.core.presentation.RunAsync

abstract class BaseSignInViewModel(
    private val authMapper: UnitResult.Mapper<AuthResultUiState>,
    protected val handleSignIn: HandleSignIn,
    runAsync: RunAsync
) : BaseAuthViewModel(handleSignIn, runAsync) {

    protected fun handleAuth(action: suspend () -> UnitResult) {
        handleSignIn.saveCredentialState(SignInCredentialUiState.Initial)
        handleSignIn.saveAuthState(AuthResultUiState.Loading)

        handle(background = action) { result ->
            handleSignIn.saveAuthState(result.map(authMapper))
        }
    }
}