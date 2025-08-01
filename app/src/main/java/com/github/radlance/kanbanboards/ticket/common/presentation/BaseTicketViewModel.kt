package com.github.radlance.kanbanboards.ticket.common.presentation

import com.github.radlance.common.presentation.BaseViewModel
import com.github.radlance.common.presentation.RunAsync
import kotlinx.coroutines.flow.StateFlow

abstract class BaseTicketViewModel(
    private val handleTicket: HandleTicket,
    runAsync: RunAsync
) : BaseViewModel(runAsync), TicketActions {

    override val ticketUiState: StateFlow<TicketUiState>
        get() = handleTicket.ticketUiState

    override fun clearCreateTicketUiState() = handleTicket.saveTicketUiState(TicketUiState.Initial)
}