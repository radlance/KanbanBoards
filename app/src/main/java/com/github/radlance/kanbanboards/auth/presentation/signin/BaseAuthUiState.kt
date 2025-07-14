package com.github.radlance.kanbanboards.auth.presentation.signin

interface AuthUiState {

    fun buttonEnabled(): Boolean

    fun hasSize(): Boolean
}

abstract class BaseAuthUiState(
    private val hasSize: Boolean,
    private val buttonEnabled: Boolean
) : AuthUiState {

    override fun buttonEnabled(): Boolean = buttonEnabled

    override fun hasSize(): Boolean = hasSize
}