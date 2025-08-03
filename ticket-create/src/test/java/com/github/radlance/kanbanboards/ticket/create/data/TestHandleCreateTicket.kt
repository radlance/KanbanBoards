package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.ticket.core.TestBaseHandleTicket
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersUiStateCreate
import com.github.radlance.kanbanboards.ticket.create.presentation.HandleCreateTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestHandleCreateTicket(
    private val testBaseHandleTicket: TestBaseHandleTicket
) : HandleCreateTicket {
    private val boardMembersUiStateMutable =
        MutableStateFlow<BoardMembersUiStateCreate>(BoardMembersUiStateCreate.Loading)
    var boardMembersUiStateCalledCount = 0
    val saveBoardMembersUiStateCalledList = mutableListOf<BoardMembersUiStateCreate>()

    override val boardMembersUiState: StateFlow<BoardMembersUiStateCreate>
        get() {
            boardMembersUiStateCalledCount++
            return boardMembersUiStateMutable
        }

    override fun saveBoardMembersUiState(boardMembersUiStateCreate: BoardMembersUiStateCreate) {
        saveBoardMembersUiStateCalledList.add(boardMembersUiStateCreate)
        boardMembersUiStateMutable.value = boardMembersUiStateCreate
    }

    override val ticketUiState: StateFlow<TicketUiState>
        get() = testBaseHandleTicket.ticketUiState

    override fun saveTicketUiState(ticketUiState: TicketUiState) {
        testBaseHandleTicket.saveTicketUiState(ticketUiState)
    }
}