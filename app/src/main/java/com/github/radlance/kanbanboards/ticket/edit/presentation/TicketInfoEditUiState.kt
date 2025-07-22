package com.github.radlance.kanbanboards.ticket.edit.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface TicketInfoEditUiState {

    @Composable
    fun Show(
        editTicketViewModel: EditTicketViewModel,
        boardId: String,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val ticket: Ticket) : TicketInfoEditUiState {

        @Composable
        override fun Show(
            editTicketViewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {

            val boardMembersUiState by editTicketViewModel.boardMembersUiState.collectAsStateWithLifecycle()

            boardMembersUiState.Show(
                ticket = ticket,
                boardId = boardId,
                ticketActions = editTicketViewModel,
                navigateUp = navigateUp,
                modifier = modifier
            )
        }
    }


    data class Error(private val message: String) : TicketInfoEditUiState {

        @Composable
        override fun Show(
            editTicketViewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) = ErrorMessage(message)
    }


    object Loading : TicketInfoEditUiState {

        @Composable
        override fun Show(
            editTicketViewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) = CircularProgressIndicator()
    }
}