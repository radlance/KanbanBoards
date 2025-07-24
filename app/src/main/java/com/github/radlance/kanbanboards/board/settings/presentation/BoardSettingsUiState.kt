package com.github.radlance.kanbanboards.board.settings.presentation

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface BoardSettingsUiState {

    @Composable
    fun Show(boardInfo: BoardInfo, modifier: Modifier = Modifier)

    data class Success(
        private val users: List<User>,
        private val members: List<User>,
    ) : BoardSettingsUiState {

        @Composable
        override fun Show(boardInfo: BoardInfo, modifier: Modifier) {
            Log.d("BoardSettingsUiState", users.toString())
            Log.d("BoardSettingsUiState", members.toString())
        }
    }

    data class Error(private val message: String) : BoardSettingsUiState {

        @Composable
        override fun Show(boardInfo: BoardInfo, modifier: Modifier) = ErrorMessage(message)
    }

    object Loading : BoardSettingsUiState {

        @Composable
        override fun Show(boardInfo: BoardInfo, modifier: Modifier) = CircularProgressIndicator()
    }
}