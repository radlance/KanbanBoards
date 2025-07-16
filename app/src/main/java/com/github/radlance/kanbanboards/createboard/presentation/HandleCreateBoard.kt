package com.github.radlance.kanbanboards.createboard.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    val createBoardUiState: StateFlow<CreateBoardUiState>

    fun saveCreateBoardUiState(boardUiState: CreateBoardUiState)

    class Base @Inject constructor() : HandleCreateBoard {

        private val createBoardUiStateMutable = MutableStateFlow<CreateBoardUiState>(
            CreateBoardUiState.CanNotCreate
        )

        override val createBoardUiState = createBoardUiStateMutable.asStateFlow()

        override fun saveCreateBoardUiState(boardUiState: CreateBoardUiState) {
            createBoardUiStateMutable.value = boardUiState
        }
    }
}