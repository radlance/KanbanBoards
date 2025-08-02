package com.github.radlance.board.settings.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.board.R
import com.github.radlance.board.settings.domain.BoardUser
import com.github.radlance.core.domain.BoardInfo
import com.github.radlance.core.domain.User
import com.github.radlance.core.presentation.BaseColumn

@Composable
fun BoardSettingsContent(
    navigateUp: () -> Unit,
    boardInfo: BoardInfo,
    users: List<User>,
    members: List<BoardUser>,
    invited: List<BoardUser>,
    boardSettingsAction: BoardSettingsAction,
    modifier: Modifier = Modifier
) {
    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    var boardNameFieldValue by rememberSaveable { mutableStateOf(boardInfo.name) }
    val boardSettingsUpdateState by boardSettingsAction.updateBoardNameUiState.collectAsStateWithLifecycle()
    val settingsFieldState by boardSettingsAction.settingsFieldState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { boardSettingsAction.resetBoardUiState() }
    }

    BaseColumn(modifier = modifier) {
        OutlinedTextField(
            value = boardNameFieldValue,
            onValueChange = {
                boardNameFieldValue = it
                boardSettingsAction.checkBoard(it)
            },
            isError = settingsFieldState.nameErrorMessage.isNotEmpty(),
            singleLine = true,
            placeholder = {
                Text(text = stringResource(com.github.radlance.core.R.string.at_least_3_symbol))
            },
            label = {
                Text(
                    text = settingsFieldState.nameErrorMessage.ifEmpty {
                        stringResource(com.github.radlance.core.R.string.board_name)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        val verticalPadding = 16.dp

        Spacer(Modifier.height(verticalPadding))

        OutlinedTextField(
            value = searchFieldValue,
            onValueChange = { searchFieldValue = it },
            singleLine = true,
            placeholder = {
                Text(text = stringResource(com.github.radlance.core.R.string.enter_email))
            },
            label = {
                Text(
                    text = stringResource(
                        com.github.radlance.core.R.string.invite_users_to_the_board
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        val dropDownOptions = remember {
            listOf(R.string.all, R.string.members, com.github.radlance.core.R.string.invitations)
        }

        var expanded by rememberSaveable { mutableStateOf(false) }
        var selectedOptionIndex by rememberSaveable { mutableIntStateOf(0) }

        val empty = searchFieldValue.isEmpty()
        val displayedUsers = (
                if (empty) {
            when (selectedOptionIndex) {
                0 -> (members + invited)
                1 -> members
                else -> invited
            }
        } else users.map { user ->
            with(user) {
                BoardUser(
                    id = (members + invited).find {
                        it.email == email
                    }?.id ?: "",
                    userId = id,
                    email = email,
                    name = name
                )
            }
        }.filter {
            it.email.contains(searchFieldValue, ignoreCase = true)
        }).sortedBy { it.email }

        when {
            !empty -> {
                Text(
                    text = stringResource(R.string.found_users),
                    modifier = Modifier
                        .padding(vertical = verticalPadding)
                        .align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalPadding)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { expanded = !expanded }
                ) {

                    Row {
                        Text(
                            text = stringResource(dropDownOptions[selectedOptionIndex]),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.show_dropdown_options)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.widthIn(min = 220.dp)
                    ) {
                        dropDownOptions.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(text = stringResource(option), fontSize = 16.sp) },
                                trailingIcon = {
                                    AnimatedVisibility(
                                        visible = selectedOptionIndex == index,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        if (selectedOptionIndex == index) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primary)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Done,
                                                    contentDescription = stringResource(
                                                        R.string.selected_dropdown_option
                                                    ),
                                                    tint = MenuDefaults.containerColor,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(3.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                onClick = {
                                    selectedOptionIndex = index
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
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

        boardSettingsUpdateState.Show(
            navigateUp = navigateUp,
            updateBoardNameAction = boardSettingsAction
        )

        val keyboardController = LocalSoftwareKeyboardController.current

        Box(modifier = Modifier.safeDrawingPadding()) {
            Button(
                onClick = {
                    if (boardNameFieldValue == boardInfo.name) {
                        navigateUp()
                    } else {
                        boardSettingsAction.updateBoardName(
                            boardInfo = boardInfo.copy(name = boardNameFieldValue)
                        )
                    }
                    keyboardController?.hide()
                },
                enabled = boardSettingsUpdateState.buttonEnabled && settingsFieldState.buttonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.update_board_name))
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}