package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

interface TicketUiState {

    @Composable
    fun Show(
        onMove: (ticketId: String, column: ColumnUi) -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val tickets: List<TicketUi>) : TicketUiState {

        @Composable
        override fun Show(
            onMove: (ticketId: String, column: ColumnUi) -> Unit,
            modifier: Modifier
        ) {
            TicketBoard(
                tickets = tickets,
                onMove = onMove,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : TicketUiState {

        @Composable
        override fun Show(
            onMove: (ticketId: String, column: ColumnUi) -> Unit,
            modifier: Modifier
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }

    object Loading : TicketUiState {

        @Composable
        override fun Show(
            onMove: (ticketId: String, column: ColumnUi) -> Unit,
            modifier: Modifier
        ) {
            CircularProgressIndicator()
        }
    }
}