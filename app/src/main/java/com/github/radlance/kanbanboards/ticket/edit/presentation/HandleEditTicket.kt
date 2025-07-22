package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.ticket.common.presentation.BaseHandleTicket
import com.github.radlance.kanbanboards.ticket.common.presentation.HandleTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleEditTicket : HandleTicket {

    val boardMembersUiState: StateFlow<BoardMembersUiStateEdit>

    fun saveBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit)

    val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>

    fun saveTicketInfoEditUiState(ticketInfoEditUiState: TicketInfoEditUiState)

    class Base @Inject constructor(): BaseHandleTicket(), HandleEditTicket {

        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiStateEdit>(
            BoardMembersUiStateEdit.Loading
        )

        private val ticketInfoEditUiStateMutable = MutableStateFlow<TicketInfoEditUiState>(
            TicketInfoEditUiState.Loading
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
    }
}