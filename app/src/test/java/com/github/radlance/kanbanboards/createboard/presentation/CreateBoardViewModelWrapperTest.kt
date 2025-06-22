package com.github.radlance.kanbanboards.createboard.presentation

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateBoardViewModelWrapperTest {

    private lateinit var viewModelWrapper: CreateBoardViewModelWrapper

    @Before
    fun setup() {
        viewModelWrapper = CreateBoardViewModelWrapper.Base(
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun test_create_board_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, viewModelWrapper.createBoardUiState().value)
        viewModelWrapper.saveCreateBoardUiState(CreateBoardUiState.Loading)
        assertEquals(CreateBoardUiState.Loading, viewModelWrapper.createBoardUiState().value)
        viewModelWrapper.saveCreateBoardUiState(CreateBoardUiState.CanCreate)
        assertEquals(CreateBoardUiState.CanCreate, viewModelWrapper.createBoardUiState().value)
        viewModelWrapper.saveCreateBoardUiState(CreateBoardUiState.Success)
        assertEquals(CreateBoardUiState.Success, viewModelWrapper.createBoardUiState().value)
        viewModelWrapper.saveCreateBoardUiState(
            CreateBoardUiState.AlreadyExists(message = "already exists")
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "already exists"),
            viewModelWrapper.createBoardUiState().value
        )

        viewModelWrapper.saveCreateBoardUiState(CreateBoardUiState.Error(message = "went wrong"))
        assertEquals(
            CreateBoardUiState.Error(message = "went wrong"),
            viewModelWrapper.createBoardUiState().value
        )
    }
}