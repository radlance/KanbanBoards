package com.github.radlance.kanbanboards.ticket.create.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleTicket {

    val boardMembersUiState: StateFlow<BoardMembersUiState>

    fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState)

    val createTicketUiState: StateFlow<CreateTicketUiState>

    fun saveCreateTicketUiState(createTicketUiState: CreateTicketUiState)

    class Base @Inject constructor() : HandleTicket {

        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiState>(
            BoardMembersUiState.Loading
        )

        private val createBoardUiStateMutable = MutableStateFlow<CreateTicketUiState>(
            CreateTicketUiState.Initial
        )

        override val boardMembersUiState: StateFlow<BoardMembersUiState>
            get() = boardMembersUiStateMutable.asStateFlow()


        override fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState) {
            boardMembersUiStateMutable.value = boardMembersUiState
        }

        override val createTicketUiState: StateFlow<CreateTicketUiState>
            get() = createBoardUiStateMutable.asStateFlow()


        override fun saveCreateTicketUiState(createTicketUiState: CreateTicketUiState) {
            createBoardUiStateMutable.value = createTicketUiState
        }
    }
}