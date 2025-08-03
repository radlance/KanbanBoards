package com.github.radlance.kanbanboards.ticket.edit.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditTicketScreen(
    navigateToBoard: () -> Unit,
    navigateUp: () -> Unit,
    boardId: String,
    modifier: Modifier = Modifier,
    viewModel: EditTicketViewModel = hiltViewModel()
) {

    val ticketInfoUiState by viewModel.ticketInfoUiState.collectAsStateWithLifecycle()

    ticketInfoUiState.Show(
        viewModel = viewModel,
        boardId = boardId,
        navigateUp = {
            navigateUp()
            viewModel.clearCreateTicketUiState()
        },
        navigateToBoard = {
            navigateToBoard()
            viewModel.clearDeleteTicketUiState()
        },
        modifier = modifier
    )
}