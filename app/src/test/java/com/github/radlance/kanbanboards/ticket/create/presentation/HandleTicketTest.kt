package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleTicketTest {

    private lateinit var handle: HandleTicket

    @Before
    fun setup() {
        handle = HandleTicket.Base()
    }

    @Test
    fun test_create_ticket_state() {
        assertEquals(CreateTicketUiState.Initial, handle.createTicketUiState.value)
        handle.saveCreateTicketUiState(CreateTicketUiState.Loading)
        assertEquals(CreateTicketUiState.Loading, handle.createTicketUiState.value)
        handle.saveCreateTicketUiState(CreateTicketUiState.Success)
        assertEquals(CreateTicketUiState.Success, handle.createTicketUiState.value)
        handle.saveCreateTicketUiState(CreateTicketUiState.Error("error message"))
        assertEquals(CreateTicketUiState.Error("error message"), handle.createTicketUiState.value)
    }

    @Test
    fun test_board_members_state() {
        assertEquals(BoardMembersUiState.Loading, handle.boardMembersUiState.value)
        handle.saveBoardMembersUiState(
            BoardMembersUiState.Success(
                members = listOf(
                    BoardMember(
                        id = "1",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            )
        )
        assertEquals(
            BoardMembersUiState.Success(
                members = listOf(
                    BoardMember(
                        id = "1",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            ),
            handle.boardMembersUiState.value
        )
        handle.saveBoardMembersUiState(BoardMembersUiState.Error("error message"))
        assertEquals(BoardMembersUiState.Error("error message"), handle.boardMembersUiState.value)
    }
}