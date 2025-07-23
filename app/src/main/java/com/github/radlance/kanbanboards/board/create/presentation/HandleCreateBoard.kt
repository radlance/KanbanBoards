package com.github.radlance.kanbanboards.board.create.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    val createBoardUiState: StateFlow<CreateBoardUiState>

    fun saveCreateBoardUiState(createBoardUiState: CreateBoardUiState)

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState)

    class Base @Inject constructor() : HandleCreateBoard {

        private val createBoardUiStateMutable = MutableStateFlow<CreateBoardUiState>(
            CreateBoardUiState.CanNotCreate
        )

        private val searchUsersUiStateMutable = MutableStateFlow<SearchUsersUiState>(
            SearchUsersUiState.Loading
        )

        override val createBoardUiState: StateFlow<CreateBoardUiState>
            get() = createBoardUiStateMutable.asStateFlow()

        override fun saveCreateBoardUiState(createBoardUiState: CreateBoardUiState) {
            createBoardUiStateMutable.value = createBoardUiState
        }

        override val searchUsersUiState = searchUsersUiStateMutable.asStateFlow()

        override fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState) {
            searchUsersUiStateMutable.value = searchUsersUiState
        }
    }
}