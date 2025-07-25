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
}

interface BoardSettingsAction : BoardSettingsMembersAction {

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>
}

interface BoardSettingsMembersAction {

    fun addUserToBoard(boardId: String, userId: String)

    fun deleteUserFromBoard(boardMemberId: String)
}