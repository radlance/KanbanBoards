package com.github.radlance.kanbanboards.board.settings.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleBoardSettings {

    val boardSettingsUiState: StateFlow<BoardSettingsUiState>

    fun saveBoardSettingsUiState(searchUsersUiState: BoardSettingsUiState)

    class Base @Inject constructor() : HandleBoardSettings {

        private val searchUsersUiStateMutable = MutableStateFlow<BoardSettingsUiState>(
            BoardSettingsUiState.Loading
        )

        override val boardSettingsUiState: StateFlow<BoardSettingsUiState>
            get() = searchUsersUiStateMutable.asStateFlow()

        override fun saveBoardSettingsUiState(searchUsersUiState: BoardSettingsUiState) {
            searchUsersUiStateMutable.value = searchUsersUiState
        }
    }
}