package com.github.radlance.kanbanboards.ticket.info.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.radlance.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.Ticket

interface TicketInfoUiState {

    @Composable
    fun Show(navigateToBoard: () -> Unit, modifier: Modifier = Modifier)

    data class Success(private val ticket: Ticket) : TicketInfoUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit, modifier: Modifier) {
            TicketInfoContent(ticket = ticket, modifier = modifier)
        }
    }

    data class Error(private val message: String) : TicketInfoUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : TicketInfoUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    object NotExists : TicketInfoUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit, modifier: Modifier) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                navigateToBoard()

                Toast.makeText(
                    context,
                    context.getString(R.string.ticket_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}