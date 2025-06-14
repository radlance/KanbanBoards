package com.github.radlance.kanbanboards.login.presentation

import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signInViewModelWrapper: SignInViewModelWrapper,
    private val signInMapper: AuthResult.Mapper<SignInResultUiState>,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    dispatchersList: DispatchersList
) : BaseViewModel(dispatchersList), SignInAction {

    val signInResultUiState = signInViewModelWrapper.signInState()

    val credentialResultUiState = signInViewModelWrapper.credentialState()

    override fun signIn(userTokenId: String) {
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
