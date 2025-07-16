package com.github.radlance.kanbanboards.ticket.create.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleTicket {

    val boardMembersUiState: StateFlow<BoardMembersUiState>

    fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState)

    class Base @Inject constructor() : HandleTicket {

        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiState>(
            BoardMembersUiState.Loading
        )

        override val boardMembersUiState: StateFlow<BoardMembersUiState> get() = boardMembersUiStateMutable.asStateFlow()

        override fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState) {
            boardMembersUiStateMutable.value = boardMembersUiState
        }
    }
}