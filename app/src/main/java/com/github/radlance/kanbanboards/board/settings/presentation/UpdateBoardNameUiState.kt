package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
import com.github.radlance.kanbanboards.common.domain.User

interface UpdateBoardNameUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        boardSettingsAction: BoardSettingsAction,
        boardInfo: BoardInfo,
        users: List<User>,
        members: List<BoardMember>,
        modifier: Modifier = Modifier
    )

    abstract class Abstract(
        private val enabled: Boolean,
        private val loading: Boolean = false,
        private val nameFieldErrorMessage: String = "",
        private val editErrorMessage: String = ""
    ) : UpdateBoardNameUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            boardInfo: BoardInfo,
            users: List<User>,
            members: List<BoardMember>,
            modifier: Modifier
        ) {
            BoardSettingsContent(
                users = users,
                members = members,
                boardInfo = boardInfo,
                boardSettingsMembersAction = boardSettingsAction,
                modifier = modifier,
                enabled = enabled,
                loading = loading,
                nameFieldErrorMessage = nameFieldErrorMessage,
                editErrorMessage = editErrorMessage
            )
        }
    }

    object Success : Abstract(
        enabled = true
    ) {
        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            boardInfo: BoardInfo,
            users: List<User>,
            members: List<BoardMember>,
            modifier: Modifier
        ) {
            super.Show(navigateUp, boardSettingsAction, boardInfo, users, members, modifier)
            navigateUp()
        }
    }

    data class AlreadyExists(private val message: String) : Abstract(
        enabled = true, nameFieldErrorMessage = message
    )

    object CanCreate : Abstract(enabled = true)

    object CanNotCreate : Abstract(enabled = false)

    object Loading : Abstract(enabled = false, loading = true)

    data class Error(private val message: String) : Abstract(
        enabled = true, editErrorMessage = message
    )
}