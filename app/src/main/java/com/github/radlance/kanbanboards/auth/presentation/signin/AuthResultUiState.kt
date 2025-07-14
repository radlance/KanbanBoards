package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import java.io.Serializable

interface AuthResultUiState : Serializable {

    @Composable
    fun Show(navigateToBoardsScreen: () -> Unit)

    fun buttonEnabled(): Boolean

    object Success : AuthResultUiState {

        private fun readResolve(): Any = Success

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = navigateToBoardsScreen()

        override fun buttonEnabled(): Boolean = true
    }

    data class Error(private val message: String) : AuthResultUiState {

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

    object Loading : AuthResultUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = CircularProgressIndicator()

        override fun buttonEnabled(): Boolean = false
    }

    object Initial : AuthResultUiState {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(navigateToBoardsScreen: () -> Unit) = Unit

        override fun buttonEnabled(): Boolean = true
    }
}