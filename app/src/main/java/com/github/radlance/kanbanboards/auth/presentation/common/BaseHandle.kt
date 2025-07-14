package com.github.radlance.kanbanboards.auth.presentation.common

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import kotlinx.coroutines.flow.StateFlow

interface BaseHandle {

    fun saveAuthState(authResultUiState: AuthResultUiState)

    fun authState(): StateFlow<AuthResultUiState>

    abstract class Abstract(
        protected val savedStateHandle: SavedStateHandle,
        private val authKey: String
    ) : BaseHandle {
        override fun saveAuthState(authResultUiState: AuthResultUiState) {
            savedStateHandle[authKey] = authResultUiState
        }

        override fun authState(): StateFlow<AuthResultUiState> {
            return savedStateHandle.getStateFlow(authKey, AuthResultUiState.Initial)
        }
    }
}