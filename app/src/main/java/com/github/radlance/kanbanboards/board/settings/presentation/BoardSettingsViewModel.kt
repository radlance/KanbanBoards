package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BoardSettingsViewModel @Inject constructor(
    private val boardSettingsRepository: BoardSettingsRepository,
    private val facade: BoardSettingsMapperFacade,
    private val handleBoardSettings: HandleBoardSettings,
    runAsync: RunAsync
) : BaseViewModel(runAsync), BoardSettingsAction {

    override val boardSettingsUiState = handleBoardSettings.boardSettingsUiState

    override val updateBoardNameUiState = handleBoardSettings.updateBoardNameUiState

    override fun deleteBoard(boardId: String) {
        handle(background = { boardSettingsRepository.deleteBoard(boardId) }, ui = {})
    }

    override fun checkBoard(name: String) {
        val uiState = if (name.trim().length >= 3) {
            UpdateBoardNameUiState.CanCreate
        } else UpdateBoardNameUiState.CanNotCreate

        handleBoardSettings.saveUpdateBoardNameUiState(uiState)
    }

    val boardUiState = handleBoardSettings.settingsBoardUiState

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

    override fun addUserToBoard(boardId: String, userId: String) {
        handle(background = { boardSettingsRepository.addUserToBoard(boardId, userId) }, ui = {})
    }

    override fun deleteUserFromBoard(boardMemberId: String) {
        handle(background = { boardSettingsRepository.deleteUserFromBoard(boardMemberId) }, ui = {})
    }

    override fun updateBoardName(boardInfo: BoardInfo) {
        handleBoardSettings.saveUpdateBoardNameUiState(UpdateBoardNameUiState.Loading)

        handle(background = { boardSettingsRepository.updateBoardName(boardInfo) }) {
            handleBoardSettings.saveUpdateBoardNameUiState(facade.mapUpdateBoardNameResult(it))
        }
    }

    override fun resetBoardUiState() {
        handleBoardSettings.saveUpdateBoardNameUiState(UpdateBoardNameUiState.CanNotCreate)
    }
}

interface BoardSettingsAction : BoardSettingsMembersAction {

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>

    val updateBoardNameUiState: StateFlow<UpdateBoardNameUiState>

    fun deleteBoard(boardId: String)
}

interface BoardSettingsMembersAction {

    fun checkBoard(name: String)

    fun addUserToBoard(boardId: String, userId: String)

    fun deleteUserFromBoard(boardMemberId: String)

    fun updateBoardName(boardInfo: BoardInfo)

    fun resetBoardUiState()
}