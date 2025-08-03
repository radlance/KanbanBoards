package com.github.radlance.kanbanboards.ticket.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface HandleTicket {

    val ticketUiState: StateFlow<TicketUiState>

    fun saveTicketUiState(ticketUiState: TicketUiState)
}

abstract class BaseHandleTicket : HandleTicket {

    private val boardUiStateMutable = MutableStateFlow<TicketUiState>(
        TicketUiState.Initial
    )

    override val ticketUiState: StateFlow<TicketUiState>
        get() = boardUiStateMutable.asStateFlow()


    override fun saveTicketUiState(ticketUiState: TicketUiState) {
        boardUiStateMutable.value = ticketUiState
    }
}