package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BoardSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardSettingsViewModel = hiltViewModel()
) {
    val boardSettingsUiState by viewModel.boardSettingsUiState.collectAsStateWithLifecycle()
}