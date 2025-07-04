package com.github.radlance.kanbanboards.createboard.presentation

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleCreateBoardTest {

    private lateinit var handle: HandleCreateBoard

    @Before
    fun setup() {
        handle = HandleCreateBoard.Base(
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun test_create_board_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, handle.createBoardUiState().value)
        handle.saveCreateBoardUiState(CreateBoardUiState.Loading)
        assertEquals(CreateBoardUiState.Loading, handle.createBoardUiState().value)
        handle.saveCreateBoardUiState(CreateBoardUiState.CanCreate)
        assertEquals(CreateBoardUiState.CanCreate, handle.createBoardUiState().value)
        handle.saveCreateBoardUiState(CreateBoardUiState.Success)
        assertEquals(CreateBoardUiState.Success, handle.createBoardUiState().value)
        handle.saveCreateBoardUiState(
            CreateBoardUiState.AlreadyExists(message = "already exists")
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "already exists"),
            handle.createBoardUiState().value
        )

        handle.saveCreateBoardUiState(CreateBoardUiState.Error(message = "went wrong"))
        assertEquals(
            CreateBoardUiState.Error(message = "went wrong"),
            handle.createBoardUiState().value
        )
    }
}