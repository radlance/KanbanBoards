package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class HandleBoardTest {

    private lateinit var handleBoard: HandleBoard

    @Before
    fun setup() {
        handleBoard = HandleBoard.Base()
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
        assertEquals(TicketUiState.Loading, handleBoard.ticketUiState.value)
        handleBoard.saveTicketUiState(TicketUiState.Error(message = "ticket error"))
        assertEquals(
            TicketUiState.Error(message = "ticket error"),
            handleBoard.ticketUiState.value
        )
        handleBoard.saveTicketUiState(
            TicketUiState.Success(
                tickets = listOf(
                    TicketUi(
                        id = "id1",
                        colorHex = "#AAAAAA",
                        name = "name1",
                        assignedMemberName = "user",
                        column = ColumnUi.InProgress
                    )
                )
            )
        )
        assertEquals(
            TicketUiState.Success(
                tickets = listOf(
                    TicketUi(
                        id = "id1",
                        colorHex = "#AAAAAA",
                        name = "name1",
                        assignedMemberName = "user",
                        column = ColumnUi.InProgress
                    )
                )
            ),
            handleBoard.ticketUiState.value
        )
    }
}