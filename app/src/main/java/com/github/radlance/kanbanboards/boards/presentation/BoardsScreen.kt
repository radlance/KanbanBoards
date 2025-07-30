package com.github.radlance.kanbanboards.boards.presentation

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import com.github.radlance.kanbanboards.invitation.presentation.InvitationCountAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(
    navigateToProfile: () -> Unit,
    navigateToBoardCreation: () -> Unit,
    navigateToBoard: (BoardInfo) -> Unit,
    navigateToInvitations: () -> Unit,
    invitationCountAction: InvitationCountAction,
    modifier: Modifier = Modifier,
    viewModel: BoardsViewModel = hiltViewModel()
) {
    val boardsUiState by viewModel.boards.collectAsStateWithLifecycle()
    val invitationsCount by invitationCountAction.invitationCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.boards))
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BadgedBox(
                            badge = {
                                val positive = invitationsCount.count > 0
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = positive,
                                    enter = scaleIn(),
                                    exit = scaleOut()
                                ) {
                                    Badge {
                                        Text(
                                            text = if (positive) {
                                                invitationsCount.count.toString()
                                            } else "1"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = navigateToInvitations
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(R.string.account_icon),
                                modifier = Modifier.padding(3.dp)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        IconButton(onClick = navigateToProfile) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = stringResource(R.string.account_icon)
                            )
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(contentPadding)
        ) {
            boardsUiState.Show(
                columnScope = this@BaseColumn,
                navigateToBoardCreation = navigateToBoardCreation,
                navigateToBoard = navigateToBoard
            )
        }
    }
}