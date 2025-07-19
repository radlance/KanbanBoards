package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
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
    private val manageResource: ManageResource,
    runAsync: RunAsync
) : BaseViewModel(runAsync), CreateBoardActions {

    val createBoardUiState = handleCreateBoard.createBoardUiState.asStateFlow()

    override val searchUsersUiState = handleCreateBoard.searchUsersUiState

    override fun checkBoard(name: String) {
        handleCreateBoard.createBoardUiState.update { currentState ->
            currentState.copy(
                nameFieldUiState = if (name.trim().length >= 3) {
                    CreateBoardUiState.CanCreate
                } else CreateBoardUiState.CanNotCreate
            )
        }
    }
    override fun createBoard(name: String, boardMembers: List<CreateUserUi>) {
        if (boardMembers.isEmpty()) {
            handleCreateBoard.createBoardUiState.update { currentState ->
                currentState.copy(
                    searchFieldErrorMessage = manageResource.string(R.string.add_at_least_one_user)
                )
            }

        } else {
            handleCreateBoard.createBoardUiState.update { currentState ->
                currentState.copy(nameFieldUiState = CreateBoardUiState.Loading)
            }

            val userIds = boardMembers.map { it.id }
            handle(background = { createBoardRepository.createBoard(name, userIds) }) {
                handleCreateBoard.createBoardUiState.update { currentState ->
                    currentState.copy(nameFieldUiState = facade.mapCreateBoardResult(it))
                }
            }
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
        handleCreateBoard.createBoardUiState.value = CreateBoardFieldsUiState()
    }

    override fun switch(userId: String, users: List<CreateUserUi>) {
        val updatedUsers = users.map { if (it.id == userId) it.copy(checked = !it.checked) else it }
        handleCreateBoard.saveSearchUsersUiState(SearchUsersUiState.Success(users = updatedUsers))
    }

    override fun clearSearchField() {
        handleCreateBoard.createBoardUiState.update { currentState ->
            currentState.copy(searchFieldErrorMessage = "")
        }
    }
}

interface UsersActions {

    fun switch(userId: String, users: List<CreateUserUi>)

    fun clearSearchField()
}

interface CreateBoardActions : UsersActions {

    fun checkBoard(name: String)

    fun createBoard(name: String, boardMembers: List<CreateUserUi>)

    val searchUsersUiState: StateFlow<SearchUsersUiState>

    fun fetchUsers()

    fun resetBoardUiState()
}