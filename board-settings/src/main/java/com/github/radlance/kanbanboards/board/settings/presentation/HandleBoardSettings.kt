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

    val settingsFieldState: MutableStateFlow<SettingsFieldState>

    val updateBoardNameUiState: StateFlow<UpdateBoardNameUiState>

    fun saveUpdateBoardNameUiState(updateBoardNameUiState: UpdateBoardNameUiState)
}

internal class BaseHandleBoardSettings @Inject constructor() : HandleBoardSettings {

    private val settingsBoardUiStateMutable = MutableStateFlow<SettingsBoardUiState>(
        SettingsBoardUiState.Loading
    )

    private val boardSettingsUiStateMutable = MutableStateFlow<BoardSettingsUiState>(
        BoardSettingsUiState.Loading
    )

    private val updateBoardNameUiStateMutable = MutableStateFlow<UpdateBoardNameUiState>(
        UpdateBoardNameUiState.Initial
    )

    private val settingsFieldStateLocal = MutableStateFlow(SettingsFieldState())

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

    override val settingsFieldState: MutableStateFlow<SettingsFieldState>
        get() = settingsFieldStateLocal

    override val updateBoardNameUiState: StateFlow<UpdateBoardNameUiState>
        get() = updateBoardNameUiStateMutable.asStateFlow()

    override fun saveUpdateBoardNameUiState(updateBoardNameUiState: UpdateBoardNameUiState) {
        updateBoardNameUiStateMutable.value = updateBoardNameUiState
    }
}