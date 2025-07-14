package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.auth.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleSignIn : BaseHandle {

    fun saveCredentialState(credentialUiState: CredentialUiState)

    fun credentialState(): StateFlow<CredentialUiState>

    fun fieldsState(): MutableStateFlow<SignInFieldsUiState>

    class Base @Inject constructor(
        savedStateHandle: SavedStateHandle
    ) : HandleSignIn, BaseHandle.Abstract(savedStateHandle, KEY_AUTH) {

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
            const val KEY_AUTH = "auth"
            const val KEY_CREDENTIAL = "credential"
            const val KEY_FIELDS = "sign in fields"
        }
    }
}