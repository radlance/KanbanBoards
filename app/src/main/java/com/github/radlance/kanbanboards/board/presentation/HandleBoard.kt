package com.github.radlance.kanbanboards.board.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleBoard {

    fun boardUiState(): StateFlow<BoardUiState>

    fun saveBoardUiState(boardUiState: BoardUiState)

    class Base @Inject constructor(private val savedStateHandle: SavedStateHandle) : HandleBoard {

        override fun boardUiState(): StateFlow<BoardUiState> {
            return savedStateHandle.getStateFlow(KEY_BOARD, BoardUiState.Loading)
        }

        override fun saveBoardUiState(boardUiState: BoardUiState) {
            savedStateHandle[KEY_BOARD] = boardUiState
        }

        companion object {
            private const val KEY_BOARD = "board"
        }
    }
}