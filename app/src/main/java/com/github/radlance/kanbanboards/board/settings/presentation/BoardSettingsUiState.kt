package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface BoardSettingsUiState {

    @Composable
    fun Show(
        boardInfo: BoardInfo,
        boardSettingsMembersAction: BoardSettingsMembersAction,
        modifier: Modifier = Modifier
    )

    data class Success(
        private val users: List<User>,
        private val members: List<BoardMember>
    ) : BoardSettingsUiState {

        @Composable
        override fun Show(
            boardInfo: BoardInfo,
            boardSettingsMembersAction: BoardSettingsMembersAction,
            modifier: Modifier
        ) {
            BoardSettingsContent(
                users = users,
                members = members,
                boardId = boardInfo.id,
                boardSettingsMembersAction = boardSettingsMembersAction,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : BoardSettingsUiState {

        @Composable
        override fun Show(
            boardInfo: BoardInfo,
            boardSettingsMembersAction: BoardSettingsMembersAction,
            modifier: Modifier
        ) = ErrorMessage(message)
    }

    object Loading : BoardSettingsUiState {

        @Composable
        override fun Show(
            boardInfo: BoardInfo,
            boardSettingsMembersAction: BoardSettingsMembersAction,
            modifier: Modifier
        ) = CircularProgressIndicator()
    }
}