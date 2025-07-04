package com.github.radlance.kanbanboards.login.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleSignIn {

    fun saveSignInState(signInResultUiState: SignInResultUiState)

    fun signInState(): StateFlow<SignInResultUiState>

    fun saveCredentialState(credentialUiState: CredentialUiState)

    fun credentialState(): StateFlow<CredentialUiState>

    class Base @Inject constructor(
        private val savedStateHandle: SavedStateHandle
    ) : HandleSignIn {

        override fun saveSignInState(signInResultUiState: SignInResultUiState) {
            savedStateHandle[KEY_SIGN_IN] = signInResultUiState
        }

        override fun signInState(): StateFlow<SignInResultUiState> {
            return savedStateHandle.getStateFlow(KEY_SIGN_IN, SignInResultUiState.Initial)
        }

        override fun saveCredentialState(credentialUiState: CredentialUiState) {
            savedStateHandle[KEY_CREDENTIAL] = credentialUiState
        }

        override fun credentialState(): StateFlow<CredentialUiState> {
            return savedStateHandle.getStateFlow(KEY_CREDENTIAL, CredentialUiState.Initial)
        }

        companion object {
            private const val KEY_SIGN_IN = "sign in"
            private const val KEY_CREDENTIAL = "credential"
        }
    }
}