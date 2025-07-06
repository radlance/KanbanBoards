package com.github.radlance.kanbanboards.board.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleBoard {

    fun boardUiState(): StateFlow<BoardUiState>

    fun saveBoardUiState(boardUiState: BoardUiState)

    fun ticketUiState(): StateFlow<TicketUiState>

    fun saveTicketUiState(ticketUiState: TicketUiState)

    class Base @Inject constructor(private val savedStateHandle: SavedStateHandle) : HandleBoard {

        override fun boardUiState(): StateFlow<BoardUiState> {
            return savedStateHandle.getStateFlow(KEY_BOARD, BoardUiState.Loading)
        }

        override fun saveBoardUiState(boardUiState: BoardUiState) {
            savedStateHandle[KEY_BOARD] = boardUiState
        }

        override fun ticketUiState(): StateFlow<TicketUiState> {
            return savedStateHandle.getStateFlow(KEY_TICKET, TicketUiState.Loading)

        }

        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            savedStateHandle[KEY_TICKET] = ticketUiState
        }

        companion object {
            private const val KEY_BOARD = "board"
            private const val KEY_TICKET = "ticket"
        }
    }
}