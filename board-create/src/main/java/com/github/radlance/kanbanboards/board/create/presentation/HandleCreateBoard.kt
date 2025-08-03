package com.github.radlance.kanbanboards.board.create.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleCreateBoard {

    val createBoardUiState: StateFlow<CreateBoardUiState>

    fun saveCreateBoardUiState(createBoardUiState: CreateBoardUiState)

    val createBoardFieldState: MutableStateFlow<CreateBoardFieldState>

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState)
}

internal class BaseHandleCreateBoard @Inject constructor() : HandleCreateBoard {

    private val createBoardUiStateMutable = MutableStateFlow<CreateBoardUiState>(
        CreateBoardUiState.Initial
    )

    private val searchUsersUiStateMutable = MutableStateFlow<SearchUsersUiState>(
        SearchUsersUiState.Loading
    )

    private val createBoardFieldStateLocal = MutableStateFlow(CreateBoardFieldState())

    override val createBoardUiState: StateFlow<CreateBoardUiState>
        get() = createBoardUiStateMutable.asStateFlow()

    override fun saveCreateBoardUiState(createBoardUiState: CreateBoardUiState) {
        createBoardUiStateMutable.value = createBoardUiState
    }

    override val createBoardFieldState get() = createBoardFieldStateLocal

    override val searchUsersUiState = searchUsersUiStateMutable.asStateFlow()

    override fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState) {
        searchUsersUiStateMutable.value = searchUsersUiState
    }
}