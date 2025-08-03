package com.github.radlance.kanbanboards.ticket.core.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface TicketActions {

    val ticketUiState: StateFlow<TicketUiState>

    fun action(
        ticketId: String,
        boardId: String,
        column: Column,
        title: String,
        color: String,
        description: String,
        assigneeId: String,
        creationDate: LocalDateTime
    )

    fun clearCreateTicketUiState()

    fun fetchBoardMembers(boardId: String, ownerId: String)
}