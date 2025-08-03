package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.core.domain.User
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class HandleEditTicketTest {

    private lateinit var handle: HandleEditTicket

    @Before
    fun setup() {
        handle = BaseHandleEditTicket()
    }

    @Test
    fun test_board_members_state() {
        assertEquals(BoardMembersUiStateEdit.Loading, handle.boardMembersUiState.value)
        handle.saveBoardMembersUiState(
            BoardMembersUiStateEdit.Success(
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
            BoardMembersUiStateEdit.Success(
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
        handle.saveBoardMembersUiState(BoardMembersUiStateEdit.Error("error message"))
        assertEquals(
            BoardMembersUiStateEdit.Error("error message"),
            handle.boardMembersUiState.value
        )
    }

    @Test
    fun test_ticket_info_state() {
        assertEquals(TicketInfoEditUiState.Loading, handle.ticketInfoEditUiState.value)
        handle.saveTicketInfoEditUiState(
            TicketInfoEditUiState.Success(
                ticket = Ticket(
                    id = "testId",
                    colorHex = "testColorHex",
                    name = "testName",
                    description = "testDescription",
                    assignedMemberName = "testAssignedMemberName",
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 5, 5, 5, 5),
                    assignedMemberId = "testAssignedMemberId"
                )
            )
        )
        assertEquals(
            TicketInfoEditUiState.Success(
                ticket = Ticket(
                    id = "testId",
                    colorHex = "testColorHex",
                    name = "testName",
                    description = "testDescription",
                    assignedMemberName = "testAssignedMemberName",
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 5, 5, 5, 5),
                    assignedMemberId = "testAssignedMemberId"
                )
            ),
            handle.ticketInfoEditUiState.value
        )
        handle.saveTicketInfoEditUiState(TicketInfoEditUiState.Error("test error message"))
        assertEquals(
            TicketInfoEditUiState.Error("test error message"),
            handle.ticketInfoEditUiState.value
        )
    }
}