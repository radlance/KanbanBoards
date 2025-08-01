package com.github.radlance.login.presentation.signin

import com.github.radlance.common.domain.UnitResult
import com.github.radlance.common.presentation.RunAsync
import com.github.radlance.login.presentation.common.BaseAuthViewModel

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