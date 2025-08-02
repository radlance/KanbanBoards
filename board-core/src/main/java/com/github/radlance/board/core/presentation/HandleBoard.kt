package com.github.radlance.board.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleBoard {

    val boardUiState: StateFlow<BoardUiState>

    fun saveBoardUiState(boardUiState: BoardUiState)

    val ticketBoardUiState: StateFlow<TicketBoardUiState>

    fun saveTicketUiState(ticketBoardUiState: TicketBoardUiState)
}

internal class BaseHandleBoard @Inject constructor() : HandleBoard {

    private val boardUiStateMutable = MutableStateFlow<BoardUiState>(BoardUiState.Loading)
    private val ticketBoardUiStateMutable = MutableStateFlow<TicketBoardUiState>(
        TicketBoardUiState.Loading
    )

    override val boardUiState get() = boardUiStateMutable.asStateFlow()

    override fun saveBoardUiState(boardUiState: BoardUiState) {
        boardUiStateMutable.value = boardUiState
    }

    override val ticketBoardUiState get() = ticketBoardUiStateMutable.asStateFlow()


    override fun saveTicketUiState(ticketBoardUiState: TicketBoardUiState) {
        ticketBoardUiStateMutable.value = ticketBoardUiState
    }
}