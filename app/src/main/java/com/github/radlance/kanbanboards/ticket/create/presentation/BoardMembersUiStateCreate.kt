package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketActions
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketScreen

interface BoardMembersUiStateCreate {

    @Composable
    fun Show(
        boardId: String,
        ticketActions: TicketActions,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val members: List<User>) : BoardMembersUiStateCreate {

        @Composable
        override fun Show(
            boardId: String,
            ticketActions: TicketActions,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {

            TicketScreen(
                boardId = boardId,
                members = members,
                ticketActions = ticketActions,
                navigateUp = navigateUp,
                buttonLabelId = R.string.create_ticket,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : BoardMembersUiStateCreate {
        @Composable
        override fun Show(
            boardId: String,
            ticketActions: TicketActions,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) = ErrorMessage(message)
    }

    object Loading : BoardMembersUiStateCreate {

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