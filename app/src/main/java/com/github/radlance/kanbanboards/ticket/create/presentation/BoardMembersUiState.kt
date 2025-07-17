package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember

interface BoardMembersUiState {

    @Composable
    fun Show(
        boardId: String,
        ticketActions: TicketActions,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val members: List<BoardMember>) : BoardMembersUiState {

        @Composable
        override fun Show(
            boardId: String,
            ticketActions: TicketActions,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {

            CreateTicketContent(
                boardId = boardId,
                members = members,
                ticketActions = ticketActions,
                navigateUp = navigateUp,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : BoardMembersUiState {
        @Composable
        override fun Show(
            boardId: String,
            ticketActions: TicketActions,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) = ErrorMessage(message)
    }

    object Loading : BoardMembersUiState {

        @Composable
        override fun Show(
            boardId: String,
            ticketActions: TicketActions,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}