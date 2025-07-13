package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.io.Serializable

interface SignInResultUiState : Serializable {

    @Composable
    fun Show(navigateToHomeScreen: () -> Unit)

    fun buttonEnabled(): Boolean

    object Success : SignInResultUiState {

        private fun readResolve(): Any = Success

        @Composable
        override fun Show(navigateToHomeScreen: () -> Unit) = navigateToHomeScreen()

        override fun buttonEnabled(): Boolean = true
    }

    data class Error(private val message: String) : SignInResultUiState {

        @Composable
        override fun Show(navigateToHomeScreen: () -> Unit) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
        }

        override fun buttonEnabled(): Boolean = true
    }

    object Loading : SignInResultUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(navigateToHomeScreen: () -> Unit) = CircularProgressIndicator()

        override fun buttonEnabled(): Boolean = false
    }

    object Initial : SignInResultUiState {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(navigateToHomeScreen: () -> Unit) = Unit

        override fun buttonEnabled(): Boolean = true
    }
}