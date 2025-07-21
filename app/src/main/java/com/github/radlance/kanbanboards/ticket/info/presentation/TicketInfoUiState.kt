package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface TicketInfoUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val ticket: Ticket) : TicketInfoUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {

            TicketInfoContent(ticket = ticket, modifier = modifier)
        }
    }

    data class Error(private val message: String) : TicketInfoUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : TicketInfoUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}