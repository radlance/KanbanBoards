package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.common.presentation.BaseColumn

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = hiltViewModel()
) {
    val boardUiState by viewModel.boardUiState.collectAsStateWithLifecycle()

    BaseColumn(verticalArrangement = Arrangement.Center) {
        boardUiState.Show(modifier.weight(1f))
    }
}