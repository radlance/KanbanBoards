package com.github.radlance.kanbanboards.createboard.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    fun createBoardUiState(): StateFlow<CreateBoardUiState>

    fun saveCreateBoardUiState(boardUiState: CreateBoardUiState)

    class Base @Inject constructor(
        private val savedStateHandle: SavedStateHandle
    ) : HandleCreateBoard {

        override fun createBoardUiState(): StateFlow<CreateBoardUiState> {
            return savedStateHandle.getStateFlow(KEY_CREATE_BOARD, CreateBoardUiState.CanNotCreate)
        }

        override fun saveCreateBoardUiState(boardUiState: CreateBoardUiState) {
            savedStateHandle[KEY_CREATE_BOARD] = boardUiState
        }

        companion object {
            private const val KEY_CREATE_BOARD = "create board"
        }
    }
}