package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.common.presentation.AbstractUnitUiState
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.common.presentation.UnitUiState

interface CreateTicketUiState : UnitUiState {

    @Composable
    fun Show(navigateUp: () -> Unit)

    object Success : AbstractUnitUiState(hasSize = false, buttonEnabled = true),
        CreateTicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) {
            navigateUp()
        }
    }

    data class Error(
        private val message: String
    ) : AbstractUnitUiState(hasSize = true, buttonEnabled = true), CreateTicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = ErrorMessage(message)
    }

    object Loading : AbstractUnitUiState(hasSize = true, buttonEnabled = false),
        CreateTicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = CircularProgressIndicator()
    }

    object Initial : AbstractUnitUiState(hasSize = false, buttonEnabled = true),
        CreateTicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = Unit
    }
}