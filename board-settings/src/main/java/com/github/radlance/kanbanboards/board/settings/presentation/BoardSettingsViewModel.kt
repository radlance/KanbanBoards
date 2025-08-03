package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.core.domain.BoardInfo
import com.github.radlance.kanbanboards.core.presentation.BaseViewModel
import com.github.radlance.kanbanboards.core.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class BoardSettingsViewModel @Inject constructor(
    private val boardSettingsRepository: BoardSettingsRepository,
    private val facade: BoardSettingsMapperFacade,
    private val handleBoardSettings: HandleBoardSettings,
    runAsync: RunAsync
) : BaseViewModel(runAsync), BoardSettingsAction {

    override val boardSettingsUiState = handleBoardSettings.boardSettingsUiState

    override val settingsFieldState = handleBoardSettings.settingsFieldState.asStateFlow()

    override val updateBoardNameUiState = handleBoardSettings.updateBoardNameUiState

    val boardUiState = handleBoardSettings.settingsBoardUiState

    override fun deleteBoard(boardId: String) {
        handle(background = { boardSettingsRepository.deleteBoard(boardId) }, ui = {})
    }

    override fun checkBoard(name: String) {
        handleBoardSettings.settingsFieldState.update { currentState ->
            currentState.copy(buttonEnabled = name.trim().length >= 3, nameErrorMessage = "")
        }
    }

    fun fetchBoard(boardInfo: BoardInfo) {
        boardSettingsRepository.board(boardInfo.id).map {
            facade.mapBoardResult(it)
        }.onStart {
            handleBoardSettings.saveSettingsBoardUiState(SettingsBoardUiState.Success(boardInfo))
        }.onEach {
            handleBoardSettings.saveSettingsBoardUiState(it)
        }.launchInViewModel()
    }

    fun fetchBoardSettings(boardId: String) {
        boardSettingsRepository.boardSettings(boardId).map {
            facade.mapBoardSettingsResult(it)
        }.onEach {
            handleBoardSettings.saveBoardSettingsUiState(it)
        }.launchInViewModel()
    }

    override fun inviteUserToBoard(boardId: String, userId: String) {
        boardSettingsRepository.inviteUserToBoard(boardId, userId, ZonedDateTime.now())
    }

    override fun rollbackInvitation(invitedMemberId: String) {
        boardSettingsRepository.rollbackInvitation(invitedMemberId)
    }

    override fun deleteUserFromBoard(boardMemberId: String) {
        boardSettingsRepository.deleteUserFromBoard(boardMemberId)
    }

    override fun updateBoardName(boardInfo: BoardInfo) {
        handleBoardSettings.saveUpdateBoardNameUiState(UpdateBoardNameUiState.Loading)

        handle(background = { boardSettingsRepository.updateBoardName(boardInfo) }) {
            handleBoardSettings.saveUpdateBoardNameUiState(facade.mapUpdateBoardNameResult(it))
        }
    }

    override fun resetBoardUiState() {
        handleBoardSettings.saveUpdateBoardNameUiState(UpdateBoardNameUiState.Initial)
        handleBoardSettings.settingsFieldState.value = SettingsFieldState()
    }

    override fun setBoardNameErrorMessage(message: String) {
        handleBoardSettings.settingsFieldState.update { currentState ->
            currentState.copy(nameErrorMessage = message)
        }
    }
}

interface BoardSettingsAction : UpdateBoardNameAction {

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>

    val settingsFieldState: StateFlow<SettingsFieldState>

    val updateBoardNameUiState: StateFlow<UpdateBoardNameUiState>

    fun deleteBoard(boardId: String)

    fun checkBoard(name: String)

    fun inviteUserToBoard(boardId: String, userId: String)

    fun rollbackInvitation(invitedMemberId: String)

    fun deleteUserFromBoard(boardMemberId: String)

    fun updateBoardName(boardInfo: BoardInfo)

    fun resetBoardUiState()
}

interface UpdateBoardNameAction {

    fun setBoardNameErrorMessage(message: String)
}