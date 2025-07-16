package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleSignIn : BaseHandle {

    fun saveCredentialState(credentialUiState: CredentialUiState)

    val credentialState: StateFlow<CredentialUiState>

    val fieldsState: MutableStateFlow<SignInFieldsUiState>

    class Base @Inject constructor() : HandleSignIn, BaseHandle.Abstract() {

        private val credentialStateFlowMutable =
            MutableStateFlow<CredentialUiState>(CredentialUiState.Initial)

        private val fieldsStateMutable = MutableStateFlow(SignInFieldsUiState())

        override fun saveCredentialState(credentialUiState: CredentialUiState) {
            credentialStateFlowMutable.value = credentialUiState
        }

        override val credentialState = credentialStateFlowMutable.asStateFlow()

        override val fieldsState = fieldsStateMutable
    }
}