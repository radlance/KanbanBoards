package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.common.presentation.BaseColumn

@Composable
fun BoardSettingsContent(
    boardInfo: BoardInfo,
    users: List<User>,
    members: List<BoardMember>,
    boardSettingsMembersAction: BoardSettingsMembersAction,
    enabled: Boolean,
    loading: Boolean,
    nameFieldErrorMessage: String,
    editErrorMessage: String,
    modifier: Modifier = Modifier
) {
    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    var boardNameFieldValue by rememberSaveable { mutableStateOf(boardInfo.name) }

    DisposableEffect(Unit) {
        onDispose { boardSettingsMembersAction.resetBoardUiState() }
    }

    BaseColumn(modifier = modifier) {
        OutlinedTextField(
            value = boardNameFieldValue,
            onValueChange = {
                boardNameFieldValue = it
                boardSettingsMembersAction.checkBoard(it)
            },
            isError = nameFieldErrorMessage.isNotEmpty(),
            singleLine = true,
            placeholder = { Text(text = stringResource(R.string.at_least_3_symbol)) },
            label = {
                Text(
                    text = nameFieldErrorMessage.ifEmpty {
                        stringResource(R.string.board_name)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = searchFieldValue,
            onValueChange = { searchFieldValue = it },
            singleLine = true,
            placeholder = { Text(text = stringResource(R.string.enter_email)) },
            label = {
                Text(text = stringResource(R.string.add_user_to_board))
            },
            modifier = Modifier.fillMaxWidth()
        )

        val empty = searchFieldValue.isEmpty()
        val displayedUsers = if (empty) {
            members
        } else users.map {
            with(it) { BoardMember(boardMemberId = "", userId = id, email = email, name = name) }
        }.filter {
            it.email.contains(searchFieldValue, ignoreCase = true)
        }

        val text = when {
            !empty -> R.string.found_users
            members.isEmpty() -> R.string.there_are_no_board_members_yet
            else -> R.string.board_members
        }

        Crossfade(text) {
            Text(
                text = stringResource(it),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = displayedUsers, key = { it.email }) { user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.animateItem()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (user.name.isNotEmpty()) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                    val contains = members.map { it.email }.contains(user.email)
                    IconButton(
                        onClick = {
                            if (!contains) {
                                boardSettingsMembersAction.addUserToBoard(
                                    boardId = boardInfo.id, userId = user.userId
                                )
                            } else {
                                boardSettingsMembersAction.deleteUserFromBoard(user.boardMemberId)
                            }
                        }
                    ) {
                        val icon = if (contains) {
                            Icons.Default.HowToReg
                        } else Icons.Default.PersonAddAlt1

                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.add_status)
                        )
                    }
                }
            }
        }

        if (editErrorMessage.isNotEmpty()) {
            Text(
                text = editErrorMessage,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
            Spacer(Modifier.weight(1f))
        }

        Box(modifier = Modifier.safeDrawingPadding()) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                val keyboardController = LocalSoftwareKeyboardController.current
                Button(
                    onClick = {
                        boardSettingsMembersAction.updateBoardName(
                            boardInfo.copy(name = boardNameFieldValue)
                        )
                        keyboardController?.hide()
                    },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.update_board_name))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}