package com.github.radlance.kanbanboards.createboard.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    val createBoardUiState: MutableStateFlow<CreateBoardFieldsUiState>

    fun saveCreateBoardUiState(boardUiState: CreateBoardFieldsUiState)

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState)

    class Base @Inject constructor() : HandleCreateBoard {

        private val createBoardUiStateMutable = MutableStateFlow(CreateBoardFieldsUiState())

        private val searchUsersUiStateMutable = MutableStateFlow<SearchUsersUiState>(
            SearchUsersUiState.Loading
        )

        override val createBoardUiState: MutableStateFlow<CreateBoardFieldsUiState> = createBoardUiStateMutable

        override fun saveCreateBoardUiState(boardUiState: CreateBoardFieldsUiState) {
            createBoardUiStateMutable.value = boardUiState
        }

        override val searchUsersUiState = searchUsersUiStateMutable.asStateFlow()

        override fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState) {
            searchUsersUiStateMutable.value = searchUsersUiState
        }
    }
}