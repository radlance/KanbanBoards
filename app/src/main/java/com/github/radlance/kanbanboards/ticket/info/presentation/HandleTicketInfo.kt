package com.github.radlance.kanbanboards.ticket.info.presentation

import com.github.radlance.kanbanboards.ticket.common.presentation.BaseHandleTicket
import com.github.radlance.kanbanboards.ticket.common.presentation.HandleTicket
import com.github.radlance.kanbanboards.ticket.edit.presentation.BoardMembersUiStateEdit
import com.github.radlance.kanbanboards.ticket.edit.presentation.TicketInfoEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleTicketInfo : HandleTicket {

    val ticketInfoUiState: StateFlow<TicketInfoUiState>

    fun saveTicketInfoUiState(ticketInfoUiState: TicketInfoUiState)

    val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>

    fun saveEditTicketUiState(ticketInfoEditUiState: TicketInfoEditUiState)

    val boardMembersUiStateEdit: StateFlow<BoardMembersUiStateEdit>

    fun saveEditBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit)

    class Base @Inject constructor() : BaseHandleTicket(), HandleTicketInfo {

        private val ticketInfoUiStateMutable = MutableStateFlow<TicketInfoUiState>(
            TicketInfoUiState.Loading
        )

        private val ticketInfoEditUiStateMutable = MutableStateFlow<TicketInfoEditUiState>(
            TicketInfoEditUiState.Loading
        )

        private val boardMembersUiStateEditMutable = MutableStateFlow<BoardMembersUiStateEdit>(
            BoardMembersUiStateEdit.Loading
        )

        override val ticketInfoUiState get() = ticketInfoUiStateMutable.asStateFlow()

        override fun saveTicketInfoUiState(ticketInfoUiState: TicketInfoUiState) {
            ticketInfoUiStateMutable.value = ticketInfoUiState
        }

        override val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>
            get() = ticketInfoEditUiStateMutable.asStateFlow()

        override fun saveEditTicketUiState(ticketInfoEditUiState: TicketInfoEditUiState) {
            ticketInfoEditUiStateMutable.value = ticketInfoEditUiState
        }

        override val boardMembersUiStateEdit: StateFlow<BoardMembersUiStateEdit>
            get() = boardMembersUiStateEditMutable.asStateFlow()

        override fun saveEditBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit) {
            boardMembersUiStateEditMutable.value = boardMembersUiStateEdit
        }
    }
}