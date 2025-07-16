package com.github.radlance.kanbanboards.board.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleBoard {

    val boardUiState: StateFlow<BoardUiState>

    fun saveBoardUiState(boardUiState: BoardUiState)

    val ticketUiState: StateFlow<TicketUiState>

    fun saveTicketUiState(ticketUiState: TicketUiState)

    class Base @Inject constructor() : HandleBoard {

        private val boardUiStateMutable = MutableStateFlow<BoardUiState>(BoardUiState.Loading)
        private val ticketUiStateMutable = MutableStateFlow<TicketUiState>(TicketUiState.Loading)

        override val boardUiState get() = boardUiStateMutable.asStateFlow()

        override fun saveBoardUiState(boardUiState: BoardUiState) {
            boardUiStateMutable.value = boardUiState
        }

        override val ticketUiState get() = ticketUiStateMutable.asStateFlow()


        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            ticketUiStateMutable.value = ticketUiState
        }
    }
}