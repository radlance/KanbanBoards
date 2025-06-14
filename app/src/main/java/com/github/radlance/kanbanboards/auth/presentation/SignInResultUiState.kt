package com.github.radlance.kanbanboards.auth.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.io.Serializable

interface SignInResultUiState : Serializable {

    @Composable
    fun Handle(navigateToHomeScreen: () -> Unit)

    object Success : SignInResultUiState {

        private fun readResolve(): Any = Success

        @Composable
        override fun Handle(navigateToHomeScreen: () -> Unit) = navigateToHomeScreen()
    }

    data class Error(private val message: String) : SignInResultUiState {

        @Composable
        override fun Handle(navigateToHomeScreen: () -> Unit) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    object Loading : SignInResultUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Handle(navigateToHomeScreen: () -> Unit) = CircularProgressIndicator()
    }

    object Initial : SignInResultUiState {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Handle(navigateToHomeScreen: () -> Unit) = Unit
    }
}