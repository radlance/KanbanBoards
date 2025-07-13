package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleSignIn {

    fun saveSignInState(signInResultUiState: SignInResultUiState)

    fun signInState(): StateFlow<SignInResultUiState>

    fun saveCredentialState(credentialUiState: CredentialUiState)

    fun credentialState(): StateFlow<CredentialUiState>

    fun fieldsState(): MutableStateFlow<SignInFieldsUiState>

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

        override fun fieldsState(): MutableStateFlow<SignInFieldsUiState> {
            return savedStateHandle.getMutableStateFlow(KEY_FIELDS, SignInFieldsUiState())
        }

        private companion object {
            const val KEY_SIGN_IN = "sign in"
            const val KEY_CREDENTIAL = "credential"
            const val KEY_FIELDS = "sign in fields"
        }
    }
}