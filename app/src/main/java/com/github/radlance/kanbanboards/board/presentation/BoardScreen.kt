package com.github.radlance.kanbanboards.board.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BoardScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = hiltViewModel()
) {
    BackHandler(onBack = navigateUp)
    val boardUiState by viewModel.boardUiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        boardUiState.Show(
            navigateUp = navigateUp,
            ticketActions = viewModel,
            modifier = modifier.weight(1f)
        )
    }
}