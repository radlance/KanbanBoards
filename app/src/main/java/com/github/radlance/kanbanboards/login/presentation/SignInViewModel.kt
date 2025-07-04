package com.github.radlance.kanbanboards.login.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val handleSignIn: HandleSignIn,
    private val signInMapper: AuthResult.Mapper<SignInResultUiState>,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    runAsync: RunAsync
) : BaseViewModel(runAsync), SignInAction {

    val signInResultUiState = handleSignIn.signInState()

    val credentialResultUiState = handleSignIn.credentialState()

    override fun signIn(userTokenId: String) {
        handleSignIn.saveCredentialState(CredentialUiState.Initial)
        handleSignIn.saveSignInState(SignInResultUiState.Loading)

        handle(background = { authRepository.signIn(userTokenId) }) { result ->
            handleSignIn.saveSignInState(result.map(signInMapper))
        }
    }

    fun createCredential(credentialResult: CredentialResult) {
        handleSignIn.saveCredentialState(credentialResult.map(credentialMapper))
    }
}

interface SignInAction {

    fun signIn(userTokenId: String)
}
