package com.github.radlance.kanbanboards.board.core.presentation

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
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo

@Composable
fun BoardScreen(
    navigateUp: () -> Unit,
    navigateToCreateTicket: (String, String) -> Unit,
    navigateToTicketInfo: (TicketUi, boardId: String, ownerId: String) -> Unit,
    navigateToBoardSettings: (boardInfo: BoardInfo) -> Unit,
    navigateToBoardsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = hiltViewModel()
) {
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
            boardTicketActions = viewModel,
            navigateToCreateTicket = navigateToCreateTicket,
            navigateToTicketInfo = navigateToTicketInfo,
            navigateToBoardSettings = navigateToBoardSettings,
            navigateToBoardsScreen = navigateToBoardsScreen,
            modifier = modifier.weight(1f)
        )
    }
}