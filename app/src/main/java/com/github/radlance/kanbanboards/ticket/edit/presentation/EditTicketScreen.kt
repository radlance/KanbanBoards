package com.github.radlance.kanbanboards.ticket.edit.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketScaffold

@Composable
fun EditTicketScreen(
    navigateUp: () -> Unit,
    boardId: String,
    modifier: Modifier = Modifier,
    viewModel: EditTicketViewModel = hiltViewModel()
) {

    val ticketInfoUiState by viewModel.ticketInfoUiState.collectAsStateWithLifecycle()
    TicketScaffold(
        navigateUp = navigateUp,
        titleResId = R.string.edit_ticket,
        modifier = modifier
    ) { ticketModifier ->
        ticketInfoUiState.Show(
            editTicketViewModel = viewModel,
            boardId = boardId,
            navigateUp = {
                navigateUp()
                viewModel.clearCreateTicketUiState()
            },
            modifier = ticketModifier
        )
    }
}