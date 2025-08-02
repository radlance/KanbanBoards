package com.github.radlance.board.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BoardSettingsScreen(
    navigateUp: () -> Unit,
    navigateToBoardsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BoardSettingsViewModel = hiltViewModel()
) {
    val boardUiState by viewModel.boardUiState.collectAsStateWithLifecycle()

    boardUiState.Show(
        modifier = modifier,
        navigateUp = navigateUp,
        navigateToBoardsScreen = navigateToBoardsScreen,
        boardSettingsAction = viewModel
    )
}