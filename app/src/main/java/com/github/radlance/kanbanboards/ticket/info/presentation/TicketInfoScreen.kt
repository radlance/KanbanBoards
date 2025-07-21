package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BackButton
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketInfoScreen(
    navigateUp: () -> Unit,
    navigateToEditTicket: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketInfoViewModel = hiltViewModel()
) {
    val ticketInfoUiState by viewModel.ticketInfoUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp, keyboardController = keyboardController)
                },
                actions = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            navigateToEditTicket()
                        },
                        modifier = modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.navigate_to_edit_ticket)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.ticket_info))
                }
            )
        },
        modifier = modifier
    ) { contentPadding ->
        ticketInfoUiState.Show(navigateUp, modifier = Modifier.padding(contentPadding))
    }
}

@Preview
@Composable
private fun TicketInfoScreenPreview() {
    KanbanBoardsTheme {
        TicketInfoScreen(navigateUp = {}, navigateToEditTicket = {})
    }
}