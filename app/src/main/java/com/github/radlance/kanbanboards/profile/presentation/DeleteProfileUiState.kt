package com.github.radlance.kanbanboards.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.radlance.core.presentation.ErrorMessage

interface DeleteProfileUiState {

    @Composable
    fun Show(navigateToSignInScreen: () -> Unit)

    object Success : DeleteProfileUiState {

        @Composable
        override fun Show(navigateToSignInScreen: () -> Unit) {
            navigateToSignInScreen()
        }
    }

    data class Error(private val message: String) : DeleteProfileUiState {

        @Composable
        override fun Show(navigateToSignInScreen: () -> Unit) {
            Spacer(Modifier.height(8.dp))
            ErrorMessage(message)
        }
    }

    object Loading : DeleteProfileUiState {

        @Composable
        override fun Show(navigateToSignInScreen: () -> Unit) {
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator()
        }
    }

    object Initial : DeleteProfileUiState {

        @Composable
        override fun Show(navigateToSignInScreen: () -> Unit) = Unit
    }
}