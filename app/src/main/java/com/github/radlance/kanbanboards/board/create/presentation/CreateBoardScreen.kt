package com.github.radlance.kanbanboards.board.create.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.common.presentation.BackButton
import com.github.radlance.common.presentation.BaseColumn
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBoardsScreen(
    navigateUp: () -> Unit,
    navigateToBoardScreen: (BoardInfo) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateBoardViewModel = hiltViewModel()
) {
    var boardNameFieldValue by rememberSaveable { mutableStateOf("") }
    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    val searchUsersUiState by viewModel.searchUsersUiState.collectAsStateWithLifecycle()
    val createBoardFieldState by viewModel.createBoardFieldState.collectAsStateWithLifecycle()
    val createBoardUiState by viewModel.createBardUiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { viewModel.resetBoardState() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },
                title = {
                    Text(text = stringResource(R.string.create_board))
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = boardNameFieldValue,
                onValueChange = {
                    boardNameFieldValue = it
                    viewModel.checkBoard(it)
                },
                singleLine = true,
                isError = createBoardFieldState.nameErrorMessage.isNotEmpty(),
                placeholder = { Text(text = stringResource(R.string.at_least_3_symbol)) },
                label = {
                    Text(
                        text = createBoardFieldState.nameErrorMessage.ifEmpty {
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
                placeholder = {
                    Text(text = stringResource(R.string.enter_email))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search_icon)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.invite_users_to_the_board)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.weight(1f)) {
                this@BaseColumn.AnimatedVisibility(
                    visible = searchFieldValue.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    searchUsersUiState.Show(
                        usersActions = viewModel,
                        searchFieldValue = searchFieldValue
                    )
                }
            }

            createBoardUiState.Show(navigateToBoardScreen, viewModel)

            val keyboardController = LocalSoftwareKeyboardController.current

            Box(modifier = Modifier.safeDrawingPadding()) {
                Button(
                    onClick = {
                        viewModel.createBoard(
                            boardNameFieldValue,
                            boardMembers = searchUsersUiState.users()
                        )
                        keyboardController?.hide()
                    },
                    enabled = createBoardFieldState.buttonEnabled && createBoardUiState.buttonEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.create))
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}