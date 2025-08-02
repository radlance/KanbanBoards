package com.github.radlance.ticket.edit.presentation

import com.github.radlance.ticket.core.presentation.BaseHandleTicket
import com.github.radlance.ticket.core.presentation.HandleTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleEditTicket : HandleTicket {

    val boardMembersUiState: StateFlow<BoardMembersUiStateEdit>

    fun saveBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit)

    val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>

    fun saveTicketInfoEditUiState(ticketInfoEditUiState: TicketInfoEditUiState)

    val deleteTicketUiState: StateFlow<DeleteTicketUiState>

    fun saveDeleteTicketUiState(deleteTicketUiState: DeleteTicketUiState)

    class Base @Inject constructor() : BaseHandleTicket(), HandleEditTicket {

        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiStateEdit>(
            BoardMembersUiStateEdit.Loading
        )

        private val ticketInfoEditUiStateMutable = MutableStateFlow<TicketInfoEditUiState>(
            TicketInfoEditUiState.Loading
        )

        private val deleteTicketUiStateMutable = MutableStateFlow<DeleteTicketUiState>(
            DeleteTicketUiState.Initial
        )

        override val boardMembersUiState: StateFlow<BoardMembersUiStateEdit>
            get() = boardMembersUiStateMutable.asStateFlow()

        override fun saveBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit) {
            boardMembersUiStateMutable.value = boardMembersUiStateEdit
        }

        override val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>
            get() = ticketInfoEditUiStateMutable.asStateFlow()

        override fun saveTicketInfoEditUiState(ticketInfoEditUiState: TicketInfoEditUiState) {
            ticketInfoEditUiStateMutable.value = ticketInfoEditUiState
        }

        override val deleteTicketUiState: StateFlow<DeleteTicketUiState>
            get() = deleteTicketUiStateMutable.asStateFlow()

        override fun saveDeleteTicketUiState(deleteTicketUiState: DeleteTicketUiState) {
            deleteTicketUiStateMutable.value = deleteTicketUiState
        }
    }
}