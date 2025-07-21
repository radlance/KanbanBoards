package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun TicketInfoScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketInfoViewModel = hiltViewModel()
) {
    val ticketInfoUiState by viewModel.ticketInfoUiState.collectAsStateWithLifecycle()
    ticketInfoUiState.Show(navigateUp, modifier = modifier)
}

@Preview
@Composable
private fun TicketInfoScreenPreview() {
    KanbanBoardsTheme {
        TicketInfoScreen(navigateUp = {})
    }
}