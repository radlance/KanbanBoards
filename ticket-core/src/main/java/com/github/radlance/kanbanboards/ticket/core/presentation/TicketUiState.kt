package com.github.radlance.kanbanboards.ticket.core.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.core.presentation.AbstractUnitUiState
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage
import com.github.radlance.kanbanboards.core.presentation.UnitUiState

interface TicketUiState : UnitUiState {

    @Composable
    fun Show(navigateUp: () -> Unit)

    object Success : AbstractUnitUiState(hasSize = false, buttonEnabled = true),
        TicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) {
            navigateUp()
        }
    }

    data class Error(
        private val message: String
    ) : AbstractUnitUiState(hasSize = true, buttonEnabled = true), TicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = ErrorMessage(message)
    }

    object Loading : AbstractUnitUiState(hasSize = true, buttonEnabled = false),
        TicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = CircularProgressIndicator()
    }

    object Initial : AbstractUnitUiState(hasSize = false, buttonEnabled = true),
        TicketUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit) = Unit
    }
}