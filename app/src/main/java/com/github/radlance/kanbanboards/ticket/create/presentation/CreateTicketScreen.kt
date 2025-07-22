package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketScaffold
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun CreateTicketScreen(
    navigateUp: () -> Unit,
    boardId: String,
    modifier: Modifier = Modifier,
    viewModel: CreateTicketViewModel = hiltViewModel()
) {
    val boardMembersUiState by viewModel.boardMembersUiState.collectAsStateWithLifecycle()

    TicketScaffold(
        navigateUp = navigateUp,
        titleResId = R.string.create_ticket
    ) { ticketModifier ->
        boardMembersUiState.Show(
            boardId = boardId,
            ticketActions = viewModel,
            navigateUp = {
                navigateUp()
                viewModel.clearCreateTicketUiState()
            },
            modifier = modifier.then(ticketModifier)
        )
    }
}

@Preview
@Composable
private fun CreateTicketScreenPreview() {
    KanbanBoardsTheme {
        CreateTicketScreen(
            navigateUp = {},
            boardId = ""
        )
    }
}