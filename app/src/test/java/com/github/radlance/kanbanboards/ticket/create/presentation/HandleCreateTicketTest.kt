package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleCreateTicketTest {

    private lateinit var handle: HandleCreateTicket

    @Before
    fun setup() {
        handle = HandleCreateTicket.Base()
    }

    @Test
    fun test_ticket_state() {
        assertEquals(TicketUiState.Initial, handle.ticketUiState.value)
        handle.saveTicketUiState(TicketUiState.Loading)
        assertEquals(TicketUiState.Loading, handle.ticketUiState.value)
        handle.saveTicketUiState(TicketUiState.Success)
        assertEquals(TicketUiState.Success, handle.ticketUiState.value)
        handle.saveTicketUiState(TicketUiState.Error("error message"))
        assertEquals(TicketUiState.Error("error message"), handle.ticketUiState.value)
    }

    @Test
    fun test_board_members_state() {
        assertEquals(BoardMembersUiStateCreate.Loading, handle.boardMembersUiState.value)
        handle.saveBoardMembersUiState(
            BoardMembersUiStateCreate.Success(
                members = listOf(
                    User(
                        id = "1",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            )
        )
        assertEquals(
            BoardMembersUiStateCreate.Success(
                members = listOf(
                    User(
                        id = "1",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            ),
            handle.boardMembersUiState.value
        )
        handle.saveBoardMembersUiState(BoardMembersUiStateCreate.Error("error message"))
        assertEquals(
            BoardMembersUiStateCreate.Error("error message"),
            handle.boardMembersUiState.value
        )
    }
}