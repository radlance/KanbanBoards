package com.github.radlance.kanbanboards.createboard.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    val createBoardUiState: StateFlow<CreateBoardUiState>

    fun saveCreateBoardUiState(boardUiState: CreateBoardUiState)

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState)

    class Base @Inject constructor() : HandleCreateBoard {

        private val createBoardUiStateMutable = MutableStateFlow<CreateBoardUiState>(
            CreateBoardUiState.CanNotCreate
        )

        private val searchUsersUiStateMutable = MutableStateFlow<SearchUsersUiState>(
            SearchUsersUiState.Loading
        )

        override val createBoardUiState = createBoardUiStateMutable.asStateFlow()

        override fun saveCreateBoardUiState(boardUiState: CreateBoardUiState) {
            createBoardUiStateMutable.value = boardUiState
        }

        override val searchUsersUiState = searchUsersUiStateMutable.asStateFlow()

        override fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState) {
            searchUsersUiStateMutable.value = searchUsersUiState
        }
    }
}