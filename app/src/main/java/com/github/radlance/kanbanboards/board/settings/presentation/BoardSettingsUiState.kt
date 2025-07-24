package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.domain.User

interface BoardSettingsUiState {

    @Composable
    fun Show()

    data class Success(
        private val users: List<User>,
        private val members: List<User>,
        private val boardInfo: BoardInfo
    ) : BoardSettingsUiState {

        @Composable
        override fun Show() {
            TODO("Not yet implemented")
        }
    }

    data class Error(private val message: String) : BoardSettingsUiState {

        @Composable
        override fun Show() {
            TODO("Not yet implemented")
        }
    }

    object Loading : BoardSettingsUiState {

        @Composable
        override fun Show() {
            TODO("Not yet implemented")
        }
    }
}