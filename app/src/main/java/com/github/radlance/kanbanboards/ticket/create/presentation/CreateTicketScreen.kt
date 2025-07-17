package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BackButton
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    navigateUp: () -> Unit,
    boardId: String,
    modifier: Modifier = Modifier,
    viewModel: TicketViewModel = hiltViewModel()
) {
    val boardMembersUiState by viewModel.boardMembersUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },
                title = {
                    Text(text = stringResource(R.string.create_ticket))
                }
            )
        }
    ) { contentPadding ->
        boardMembersUiState.Show(
            boardId = boardId,
            ticketActions = viewModel,
            navigateUp = {
                navigateUp()
                viewModel.clearCreateTicketUiState()
            },
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
            )
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