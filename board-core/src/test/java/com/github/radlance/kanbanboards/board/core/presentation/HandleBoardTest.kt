package com.github.radlance.kanbanboards.board.core.presentation

import com.github.radlance.kanbanboards.core.domain.BoardInfo
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test

class HandleBoardTest {

    private lateinit var handleBoard: HandleBoard

    @Before
    fun setup() {
        handleBoard = BaseHandleBoard()
    }

    @Test
    fun test_board_ui_state() {
        assertEquals(BoardUiState.Loading, handleBoard.boardUiState.value)
        handleBoard.saveBoardUiState(BoardUiState.Error(message = "board error"))
        assertEquals(BoardUiState.Error("board error"), handleBoard.boardUiState.value)
        handleBoard.saveBoardUiState(
            BoardUiState.Success(
                boardInfo = BoardInfo(
                    id = "id1",
                    name = "name1",
                    isMyBoard = false,
                    owner = "user1"
                )
            )
        )
        assertEquals(
            BoardUiState.Success(
                boardInfo = BoardInfo(
                    id = "id1",
                    name = "name1",
                    isMyBoard = false,
                    owner = "user1"
                )
            ),
            handleBoard.boardUiState.value
        )
    }

    @Test
    fun test_ticket_ui_state() {
        assertEquals(TicketBoardUiState.Loading, handleBoard.ticketBoardUiState.value)
        handleBoard.saveTicketUiState(TicketBoardUiState.Error(message = "ticket error"))
        assertEquals(
            TicketBoardUiState.Error(message = "ticket error"),
            handleBoard.ticketBoardUiState.value
        )
        handleBoard.saveTicketUiState(
            TicketBoardUiState.Success(
                tickets = listOf(
                    TicketUi(
                        id = "id1",
                        colorHex = "#AAAAAA",
                        name = "name1",
                        description = "description1",
                        assignedMemberNames = "user",
                        column = ColumnUi.InProgress,
                        creationDate = LocalDateTime(2024, 1, 10, 5, 50),
                        assignedMemberIds = "assigned member id1"
                    )
                )
            )
        )
        assertEquals(
            TicketBoardUiState.Success(
                tickets = listOf(
                    TicketUi(
                        id = "id1",
                        colorHex = "#AAAAAA",
                        name = "name1",
                        description = "description1",
                        assignedMemberNames = "user",
                        column = ColumnUi.InProgress,
                        creationDate = LocalDateTime(2024, 1, 10, 5, 50),
                        assignedMemberIds = "assigned member id1"
                    )
                )
            ),
            handleBoard.ticketBoardUiState.value
        )
    }
}