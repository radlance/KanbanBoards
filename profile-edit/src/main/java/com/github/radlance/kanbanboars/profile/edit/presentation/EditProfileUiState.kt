package com.github.radlance.kanbanboars.profile.edit.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.core.presentation.AbstractUnitUiState
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage
import com.github.radlance.kanbanboards.core.presentation.UnitUiState

interface EditProfileUiState : UnitUiState {

    @Composable
    fun Show(navigateUp: () -> Unit)

    object Success : EditProfileUiState, AbstractUnitUiState(
        hasSize = false, buttonEnabled = true
    ) {

        @Composable
        override fun Show(navigateUp: () -> Unit) {
            navigateUp()
        }
    }

    data class Error(private val message: String) : EditProfileUiState, AbstractUnitUiState(
        hasSize = true, buttonEnabled = true
    ) {
        @Composable
        override fun Show(navigateUp: () -> Unit) = ErrorMessage(message)
    }

    object Loading : EditProfileUiState, AbstractUnitUiState(
        hasSize = true, buttonEnabled = false
    ) {
        @Composable
        override fun Show(navigateUp: () -> Unit) = CircularProgressIndicator()
    }

    object Initial : EditProfileUiState, AbstractUnitUiState(
        hasSize = false, buttonEnabled = true
    ) {
        @Composable
        override fun Show(navigateUp: () -> Unit) = Unit
    }
}