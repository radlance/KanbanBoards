package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import java.io.Serializable

interface TicketUiState : Serializable {

    @Composable
    fun Show(modifier: Modifier = Modifier)

    data class Success(private val tickets: List<TicketUi>) : TicketUiState {

        @Composable
        override fun Show(modifier: Modifier) {
            TicketBoard(tickets = tickets, modifier = modifier)
        }
    }

    data class Error(private val message: String) : TicketUiState {

        @Composable
        override fun Show(modifier: Modifier) {
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

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(modifier: Modifier) {
            CircularProgressIndicator()
        }
    }
}