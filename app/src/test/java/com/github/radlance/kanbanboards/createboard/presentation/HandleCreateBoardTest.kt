package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleCreateBoardTest {

    private lateinit var handle: HandleCreateBoard

    @Before
    fun setup() {
        handle = HandleCreateBoard.Base()
    }

    @Test
    fun test_create_board_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, handle.createBoardUiState.value)
        handle.saveCreateBoardUiState(CreateBoardUiState.Loading)
        assertEquals(CreateBoardUiState.Loading, handle.createBoardUiState.value)
        handle.saveCreateBoardUiState(CreateBoardUiState.CanCreate)
        assertEquals(CreateBoardUiState.CanCreate, handle.createBoardUiState.value)
        handle.saveCreateBoardUiState(
            CreateBoardUiState.Success(
                BoardInfo(id = "123", name = "some board123", isMyBoard = false)
            )
        )
        assertEquals(
            CreateBoardUiState.Success(
                BoardInfo(id = "123", name = "some board123", isMyBoard = false)
            ),
            handle.createBoardUiState.value
        )
        handle.saveCreateBoardUiState(
            CreateBoardUiState.AlreadyExists(message = "already exists")
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "already exists"),
            handle.createBoardUiState.value
        )

        handle.saveCreateBoardUiState(CreateBoardUiState.Error(message = "went wrong"))
        assertEquals(
            CreateBoardUiState.Error(message = "went wrong"),
            handle.createBoardUiState.value
        )
    }
}