package com.github.radlance.kanbanboards.ticket.core

import com.github.radlance.kanbanboards.ticket.core.presentation.HandleTicket
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestBaseHandleTicket : HandleTicket {
    var ticketUiStateCalledCount = 0
    val saveCreateTicketUiStateCalledList = mutableListOf<TicketUiState>()

    private val createTicketUiStateMutable =
        MutableStateFlow<TicketUiState>(TicketUiState.Initial)

    override val ticketUiState: StateFlow<TicketUiState>
        get() {
            ticketUiStateCalledCount++
            return createTicketUiStateMutable
        }

    override fun saveTicketUiState(ticketUiState: TicketUiState) {
        saveCreateTicketUiStateCalledList.add(ticketUiState)
        createTicketUiStateMutable.value = ticketUiState
    }
}