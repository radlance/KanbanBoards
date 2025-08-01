package com.github.radlance.login.presentation.signin

import com.github.radlance.login.presentation.common.AbstractBaseHandle
import com.github.radlance.login.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleSignIn : BaseHandle {

    fun saveCredentialState(signInCredentialUiState: SignInCredentialUiState)

    val credentialState: StateFlow<SignInCredentialUiState>

    val fieldsState: MutableStateFlow<SignInFieldsUiState>
}

internal class BaseHandleSignIn @Inject constructor() : HandleSignIn, AbstractBaseHandle() {

    private val credentialStateFlowMutable =
        MutableStateFlow<SignInCredentialUiState>(SignInCredentialUiState.Initial)

    private val fieldsStateMutable = MutableStateFlow(SignInFieldsUiState())

    override fun saveCredentialState(signInCredentialUiState: SignInCredentialUiState) {
        credentialStateFlowMutable.value = signInCredentialUiState
    }

    override val credentialState = credentialStateFlowMutable.asStateFlow()

    override val fieldsState = fieldsStateMutable
}