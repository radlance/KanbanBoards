package com.github.radlance.board.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.radlance.board.settings.domain.BoardUser
import com.github.radlance.core.domain.BoardInfo

@Composable
internal fun DisplayedUserList(
    displayedUsers: List<BoardUser>,
    invited: List<BoardUser>,
    members: List<BoardUser>,
    boardSettingsAction: BoardSettingsAction,
    boardInfo: BoardInfo,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = displayedUsers, key = { it.email + it.id }) { user ->
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
                val hasInvitation = invited.map { it.email }.contains(user.email)
                val isMember = members.map { it.email }.contains(user.email)

                IconButton(
                    onClick = {
                        when {
                            isMember -> {
                                boardSettingsAction.deleteUserFromBoard(user.id)
                            }

                            hasInvitation -> {
                                boardSettingsAction.rollbackInvitation(user.id)
                            }

                            else -> boardSettingsAction.inviteUserToBoard(
                                boardId = boardInfo.id, userId = user.userId
                            )
                        }
                    }
                ) {
                    val icon = when {

                        isMember -> Icons.Default.HowToReg

                        hasInvitation -> Icons.Default.MarkEmailRead

                        else -> Icons.Default.PersonAddAlt1
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(
                            com.github.radlance.core.R.string.add_status
                        )
                    )
                }
            }
        }
    }
}