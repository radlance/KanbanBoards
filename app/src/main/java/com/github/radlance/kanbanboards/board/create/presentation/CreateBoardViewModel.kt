package com.github.radlance.kanbanboards.board.create.presentation

import com.github.radlance.common.presentation.BaseViewModel
import com.github.radlance.common.presentation.RunAsync
import com.github.radlance.kanbanboards.board.create.domain.CreateBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateBoardViewModel @Inject constructor(
    private val createBoardRepository: CreateBoardRepository,
    private val handleCreateBoard: HandleCreateBoard,
    private val facade: CreateBoardMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync), CreateBoardActions {

    val createBardUiState = handleCreateBoard.createBoardUiState

    val createBoardFieldState = handleCreateBoard.createBoardFieldState.asStateFlow()

    val searchUsersUiState = handleCreateBoard.searchUsersUiState

    fun checkBoard(name: String) {
        handleCreateBoard.createBoardFieldState.update { currentState ->
            currentState.copy(buttonEnabled = name.trim().length >= 3, nameErrorMessage = "")
        }
    }

    fun createBoard(name: String, boardMembers: List<CreateUserUi>) {

        handleCreateBoard.saveCreateBoardUiState(CreateBoardUiState.Loading)

        val userIds = boardMembers.map { it.id }

        handle(background = { createBoardRepository.createBoard(name, userIds) }) {
            handleCreateBoard.saveCreateBoardUiState(facade.mapCreateBoardResult(it))
        }
    }

    fun fetchUsers() {
        createBoardRepository.users().map {
            facade.mapSearchUsersResult(it)
        }.onEach {
            handleCreateBoard.saveSearchUsersUiState(it)
        }.launchInViewModel()
    }

    override fun setBoardNameErrorMessage(message: String) {
        handleCreateBoard.createBoardFieldState.update { currentState ->
            currentState.copy(nameErrorMessage = message)
        }
    }

    fun resetBoardState() {
        handleCreateBoard.saveCreateBoardUiState(CreateBoardUiState.Initial)
        handleCreateBoard.createBoardFieldState.value = CreateBoardFieldState()
    }

    override fun switch(userId: String, users: List<CreateUserUi>) {
        val updatedUsers = users.map { if (it.id == userId) it.copy(checked = !it.checked) else it }
        handleCreateBoard.saveSearchUsersUiState(SearchUsersUiState.Success(users = updatedUsers))
    }
}

interface UsersActions {

    fun switch(userId: String, users: List<CreateUserUi>)
}

interface CreateBoardActions : UsersActions {

    fun setBoardNameErrorMessage(message: String)
}