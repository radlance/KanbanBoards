package com.github.radlance.kanbanboards.ticket.info.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class HandleTicketInfoTest {

    private lateinit var handle: HandleTicketInfo

    @Before
    fun setup() {
        handle = BaseHandleTicketInfo()
    }

    @Test
    fun test_ticket_info_state() {
        assertEquals(TicketInfoUiState.Loading, handle.ticketInfoUiState.value)
        handle.saveTicketInfoUiState(
            TicketInfoUiState.Success(
                ticket = Ticket(
                    id = "testId",
                    colorHex = "testColorHex",
                    name = "testName",
                    description = "testDescription",
                    assignedMemberNames = listOf("testAssignedMemberName"),
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 5, 5, 5, 5),
                    assignedMemberIds = listOf("testAssignedMemberId")
                )
            )
        )
        assertEquals(
            TicketInfoUiState.Success(
                ticket = Ticket(
                    id = "testId",
                    colorHex = "testColorHex",
                    name = "testName",
                    description = "testDescription",
                    assignedMemberNames = listOf("testAssignedMemberName"),
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 5, 5, 5, 5),
                    assignedMemberIds = listOf("testAssignedMemberId")
                )
            ),
            handle.ticketInfoUiState.value
        )
        handle.saveTicketInfoUiState(TicketInfoUiState.Error("test error message"))
        assertEquals(TicketInfoUiState.Error("test error message"), handle.ticketInfoUiState.value)
    }
}