package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import java.io.Serializable

interface AuthResultUiState : Serializable, AuthUiState {

    @Composable
    fun Show(navigateToBoardsScreen: () -> Unit)

    object Success : AuthResultUiState, BaseAuthUiState(hasSize = false, buttonEnabled = true) {

        private fun readResolve(): Any = Success

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = navigateToBoardsScreen()

        override fun buttonEnabled(): Boolean = true
    }

    data class Error(
        private val message: String
    ) : AuthResultUiState, BaseAuthUiState(hasSize = true, buttonEnabled = true) {

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        override fun buttonEnabled(): Boolean = true
    }

    object Loading : AuthResultUiState, BaseAuthUiState(hasSize = true, buttonEnabled = false) {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = CircularProgressIndicator()

        override fun buttonEnabled(): Boolean = false
    }

    object Initial : AuthResultUiState, BaseAuthUiState(hasSize = false, buttonEnabled = true) {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = Unit

        override fun buttonEnabled(): Boolean = true
    }
}