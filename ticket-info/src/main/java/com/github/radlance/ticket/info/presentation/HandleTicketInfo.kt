package com.github.radlance.ticket.info.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleTicketInfo {

    val ticketInfoUiState: StateFlow<TicketInfoUiState>

    fun saveTicketInfoUiState(ticketInfoUiState: TicketInfoUiState)

    class Base @Inject constructor() : HandleTicketInfo {

        private val ticketInfoUiStateMutable = MutableStateFlow<TicketInfoUiState>(
            TicketInfoUiState.Loading
        )

        override val ticketInfoUiState get() = ticketInfoUiStateMutable.asStateFlow()

        override fun saveTicketInfoUiState(ticketInfoUiState: TicketInfoUiState) {
            ticketInfoUiStateMutable.value = ticketInfoUiState
        }
    }
}