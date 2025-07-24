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

    fun fetchBoardSettings(boardId: String, ownerId: String) {
        boardSettingsRepository.boardSettings(boardId, ownerId).map {
            facade.mapBoardSettingsResult(it)
        }.onEach {
            handleBoardSettings.saveBoardSettingsUiState(it)
        }.launchInViewModel()
    }
}

interface BoardSettingsAction {

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>
}