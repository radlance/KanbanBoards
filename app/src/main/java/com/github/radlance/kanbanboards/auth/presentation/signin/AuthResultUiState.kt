package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.common.presentation.AbstractUnitUiState
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.common.presentation.UnitUiState

interface AuthResultUiState : UnitUiState {

    @Composable
    fun Show(navigateToBoardsScreen: () -> Unit)

    object Success : AuthResultUiState, AbstractUnitUiState(hasSize = false, buttonEnabled = true) {

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = navigateToBoardsScreen()
    }

    data class Error(
        private val message: String
    ) : AuthResultUiState, AbstractUnitUiState(hasSize = true, buttonEnabled = true) {

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = ErrorMessage(message)
    }

    object Loading : AuthResultUiState, AbstractUnitUiState(hasSize = true, buttonEnabled = false) {

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = CircularProgressIndicator()
    }

    object Initial : AuthResultUiState, AbstractUnitUiState(hasSize = false, buttonEnabled = true) {

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = Unit
    }
}