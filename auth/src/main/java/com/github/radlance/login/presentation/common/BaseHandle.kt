package com.github.radlance.login.presentation.common

import com.github.radlance.login.presentation.signin.AuthResultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface BaseHandle {

    fun saveAuthState(authResultUiState: AuthResultUiState)

    val authState: StateFlow<AuthResultUiState>
}

internal abstract class AbstractBaseHandle : BaseHandle {

    private val stateFlow = MutableStateFlow<AuthResultUiState>(AuthResultUiState.Initial)

    override fun saveAuthState(authResultUiState: AuthResultUiState) {
        stateFlow.value = authResultUiState
    }

    override val authState = stateFlow.asStateFlow()
}