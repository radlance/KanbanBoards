package com.github.radlance.board.settings.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.board.R
import com.github.radlance.core.domain.BoardInfo
import com.github.radlance.core.presentation.BackButton
import com.github.radlance.core.presentation.ErrorMessage

interface SettingsBoardUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        navigateToBoardsScreen: () -> Unit,
        boardSettingsAction: BoardSettingsAction,
        modifier: Modifier = Modifier
    )

    data class Success(private val boardInfo: BoardInfo) : SettingsBoardUiState {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            val boardSettingsUiState by boardSettingsAction.boardSettingsUiState.collectAsStateWithLifecycle()

            var showAlertDialog by rememberSaveable { mutableStateOf(false) }

            if (showAlertDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showAlertDialog = false
                                boardSettingsAction.deleteBoard(boardInfo.id)
                            }
                        ) {
                            Text(text = stringResource(com.github.radlance.core.R.string.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAlertDialog = false }) {
                            Text(text = stringResource(com.github.radlance.core.R.string.cancel))
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.delete_board),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    icon = {
                        Icon(imageVector = Icons.Outlined.Warning, contentDescription = null)
                    },
                    text = {
                        Text(
                            text = stringResource(
                                com.github.radlance.core.R.string.are_you_sure_you_want_to_delete,
                                boardInfo.name
                            )
                        )
                    }
                )
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = stringResource(com.github.radlance.core.R.string.settings))
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    keyboardController?.hide()
                                    showAlertDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = stringResource(
                                        com.github.radlance.core.R.string.delete
                                    )
                                )
                            }
                        },
                        navigationIcon = { BackButton(navigateUp) }
                    )
                },
                modifier = modifier
            ) { contentPadding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            top = contentPadding.calculateTopPadding(),
                            start = contentPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    boardSettingsUiState.Show(
                        boardInfo = boardInfo,
                        boardSettingsAction = boardSettingsAction,
                        navigateUp = navigateUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    data class Error(private val message: String) : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message = message)
            }
        }
    }

    object Loading : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    object NotExists : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                navigateToBoardsScreen()
                Toast.makeText(
                    context,
                    R.string.the_board_has_been_deleted,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}