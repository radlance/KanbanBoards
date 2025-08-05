package com.github.radlance.kanbanboards.ticket.edit.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.core.domain.User
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketActions
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketScreen
import com.github.radlance.ticket.R

interface BoardMembersUiStateEdit {

    @Composable
    fun Show(
        ticket: Ticket,
        ticketActions: TicketActions,
        boardId: String,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val members: List<User>) : BoardMembersUiStateEdit {

        @Composable
        override fun Show(
            ticket: Ticket,
            ticketActions: TicketActions,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            with(ticket) {
                TicketScreen(
                    boardId = boardId,
                    members = members,
                    ticketActions = ticketActions,
                    navigateUp = navigateUp,
                    selectedColor = colorHex,
                    initialTitleFieldValue = name,
                    initialSelectedAssignedList = assignedMemberIds,
                    initialDescriptionFieldValue = description,
                    ticketId = id,
                    column = column,
                    creationDate = creationDate,
                    buttonLabelId = R.string.edit_ticket,
                    modifier = modifier
                )
            }
        }
    }

    data class Error(private val message: String) : BoardMembersUiStateEdit {
        @Composable
        override fun Show(
            ticket: Ticket,
            ticketActions: TicketActions,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) = ErrorMessage(message)
    }

    object Loading : BoardMembersUiStateEdit {

        @Composable
        override fun Show(
            ticket: Ticket,
            ticketActions: TicketActions,
            boardId: String,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}