package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface BoardSettingsUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        boardInfo: BoardInfo,
        boardSettingsAction: BoardSettingsAction,
        modifier: Modifier = Modifier
    )

    data class Success(
        private val users: List<User>,
        private val members: List<BoardMember>
    ) : BoardSettingsUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            boardInfo: BoardInfo,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            val boardSettingsUpdateState by boardSettingsAction.updateBoardNameUiState.collectAsStateWithLifecycle()

            boardSettingsUpdateState.Show(
                navigateUp = navigateUp,
                boardSettingsAction = boardSettingsAction,
                boardInfo = boardInfo,
                users = users,
                members = members,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : BoardSettingsUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            boardInfo: BoardInfo,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) = ErrorMessage(message)
    }

    object Loading : BoardSettingsUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            boardInfo: BoardInfo,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) = CircularProgressIndicator()
    }
}