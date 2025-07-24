package com.github.radlance.kanbanboards.board.settings.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleBoardSettings {

    val settingsBoardUiState: StateFlow<SettingsBoardUiState>

    fun saveSettingsBoardUiState(settingsBoardUiState: SettingsBoardUiState)

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>

    fun saveBoardSettingsUiState(boardSettingsUiState: BoardSettingsUiState)

    class Base @Inject constructor() : HandleBoardSettings {

        private val settingsBoardUiStateMutable = MutableStateFlow<SettingsBoardUiState>(
            SettingsBoardUiState.Loading
        )

        private val boardSettingsUiStateMutable = MutableStateFlow<BoardSettingsUiState>(
            BoardSettingsUiState.Loading
        )
        override val settingsBoardUiState: StateFlow<SettingsBoardUiState>
            get() = settingsBoardUiStateMutable.asStateFlow()

        override fun saveSettingsBoardUiState(settingsBoardUiState: SettingsBoardUiState) {
            settingsBoardUiStateMutable.value = settingsBoardUiState
        }

        override val boardSettingsUiState: StateFlow<BoardSettingsUiState>
            get() = boardSettingsUiStateMutable.asStateFlow()

        override fun saveBoardSettingsUiState(boardSettingsUiState: BoardSettingsUiState) {
            boardSettingsUiStateMutable.value = boardSettingsUiState
        }
    }
}