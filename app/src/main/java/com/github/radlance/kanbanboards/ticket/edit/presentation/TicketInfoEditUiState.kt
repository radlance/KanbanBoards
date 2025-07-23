package com.github.radlance.kanbanboards.ticket.edit.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketScaffold

interface TicketInfoEditUiState {

    @Composable
    fun Show(
        viewModel: EditTicketViewModel,
        boardId: String,
        navigateUp: () -> Unit,
        navigateToBoard: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val ticket: Ticket) : TicketInfoEditUiState {

        @Composable
        override fun Show(
            viewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            navigateToBoard: () -> Unit,
            modifier: Modifier
        ) {
            val deleteTicketUiState by viewModel.deleteTicketUiState.collectAsStateWithLifecycle()
            val boardMembersUiState by viewModel.boardMembersUiState.collectAsStateWithLifecycle()
            var showAlertDialog by rememberSaveable { mutableStateOf(false) }

            deleteTicketUiState.Show()

            if (showAlertDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showAlertDialog = false
                                viewModel.deleteTicket(ticket.id)
                            }
                        ) {
                            Text(text = stringResource(R.string.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAlertDialog = false }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.delete_ticket),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    icon = {
                        Icon(imageVector = Icons.Outlined.Warning, contentDescription = null)
                    },
                    text = {
                        Text(
                            text = stringResource(
                                R.string.are_you_sure_you_want_to_delete, ticket.name
                            )
                        )
                    }
                )
            }

            TicketScaffold(
                navigateUp = navigateUp,
                titleResId = R.string.edit_ticket,
                actions = {
                    IconButton(onClick = { showAlertDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            ) { ticketModifier ->
                boardMembersUiState.Show(
                    ticket = ticket,
                    boardId = boardId,
                    ticketActions = viewModel,
                    navigateUp = navigateUp,
                    modifier = modifier.then(ticketModifier)
                )
            }
        }
    }


    data class Error(private val message: String) : TicketInfoEditUiState {

        @Composable
        override fun Show(
            viewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            navigateToBoard: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }


    object Loading : TicketInfoEditUiState {

        @Composable
        override fun Show(
            viewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            navigateToBoard: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    object NotExists : TicketInfoEditUiState {

        @Composable
        override fun Show(
            viewModel: EditTicketViewModel,
            boardId: String,
            navigateUp: () -> Unit,
            navigateToBoard: () -> Unit,
            modifier: Modifier
        ) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                navigateToBoard()

                Toast.makeText(
                    context,
                    context.getString(R.string.ticket_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}