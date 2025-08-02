package com.github.radlance.ticket.edit.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.board.core.domain.Ticket
import com.github.radlance.core.domain.User
import com.github.radlance.core.presentation.ErrorMessage
import com.github.radlance.ticket.R
import com.github.radlance.ticket.common.presentation.TicketActions
import com.github.radlance.ticket.common.presentation.TicketScreen

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
                    initialSelectedAssigneeId = assignedMemberId,
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