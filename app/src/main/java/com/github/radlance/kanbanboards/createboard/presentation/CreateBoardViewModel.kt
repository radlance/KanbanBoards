package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateBoardViewModel @Inject constructor(
    private val createBoardRepository: CreateBoardRepository,
    private val handleCreateBoard: HandleCreateBoard,
    private val facade: CreateBoardMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync), CreateBoardActions {

    val createBoardUiState = handleCreateBoard.createBoardUiState

    override val searchUsersUiState = handleCreateBoard.searchUsersUiState

    override fun checkBoard(name: String) {
        val uiState: CreateBoardUiState = if (name.trim().length >= 3) {
            CreateBoardUiState.CanCreate
        } else CreateBoardUiState.CanNotCreate

        handleCreateBoard.saveCreateBoardUiState(uiState)
    }
    override fun createBoard(name: String, boardMembers: List<CreateUserUi>) {
        handleCreateBoard.saveCreateBoardUiState(CreateBoardUiState.Loading)

        val userIds = boardMembers.map { it.id }
        handle(background = { createBoardRepository.createBoard(name, userIds) }) {
            handleCreateBoard.saveCreateBoardUiState(facade.mapCreateBoardResult(it))
        }
    }

    override fun fetchUsers() {
        createBoardRepository.users().map {
            facade.mapSearchUsersResult(it)
        }.onEach {
            handleCreateBoard.saveSearchUsersUiState(it)
        }.launchInViewModel()
    }

    override fun resetBoardUiState() {
        handleCreateBoard.saveCreateBoardUiState(CreateBoardUiState.CanNotCreate)
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

    fun checkBoard(name: String)

    fun createBoard(name: String, boardMembers: List<CreateUserUi>)

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun fetchUsers()

    fun resetBoardUiState()
}