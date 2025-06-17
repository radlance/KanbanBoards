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
    private val signInViewModelWrapper: SignInViewModelWrapper,
    private val signInMapper: AuthResult.Mapper<SignInResultUiState>,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    runAsync: RunAsync
) : BaseViewModel(runAsync), SignInAction {

    val signInResultUiState = signInViewModelWrapper.signInState()

    val credentialResultUiState = signInViewModelWrapper.credentialState()

    override fun signIn(userTokenId: String) {
        signInViewModelWrapper.saveCredentialState(CredentialUiState.Initial)
        signInViewModelWrapper.saveSignInState(SignInResultUiState.Loading)

        handle(background = { authRepository.signIn(userTokenId) }) { result ->
            signInViewModelWrapper.saveSignInState(result.map(signInMapper))
        }
    }

    fun createCredential(credentialResult: CredentialResult) {
        signInViewModelWrapper.saveCredentialState(credentialResult.map(credentialMapper))
    }
}

interface SignInAction {

    fun signIn(userTokenId: String)
}
