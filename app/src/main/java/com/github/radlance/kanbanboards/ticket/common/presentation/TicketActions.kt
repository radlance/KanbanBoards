package com.github.radlance.kanbanboards.ticket.common.presentation

import com.github.radlance.kanbanboards.ticket.create.presentation.TicketUiState
import kotlinx.coroutines.flow.StateFlow

interface TicketActions {

    val ticketUiState: StateFlow<TicketUiState>

    fun action(
        boardId: String,
        title: String,
        color: String,
        description: String,
        assigneeId: String
    )

    fun clearCreateTicketUiState()

    fun fetchBoardMembers(boardId: String, ownerId: String)
}