package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }
    var showBoardMembers by rememberSaveable { mutableStateOf(false) }
    var selectedColorIndex by rememberSaveable { mutableIntStateOf(0) }
    var titleFieldValue by rememberSaveable { mutableStateOf("") }
    var assigneeFieldValue by rememberSaveable { mutableStateOf("") }
    var descriptionFieldValue by rememberSaveable { mutableStateOf("") }

    val ticketColors = remember {
        listOf(
            TicketColor.Yellow,
            TicketColor.Orange,
            TicketColor.Purple,
            TicketColor.Red,
            TicketColor.Green,
            TicketColor.Blue
        )
    }

    val boardMembersUiState by viewModel.boardMembersUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.create_ticket))
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(
            modifier = modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = titleFieldValue,
                    onValueChange = { titleFieldValue = it },
                    label = { Text(text = stringResource(R.string.title)) },
                    singleLine = true,
                    placeholder = { Text(text = stringResource(R.string.at_least_3_symbol)) },
                    modifier = Modifier.fillMaxWidth()
                )

                TicketColorsRow(
                    colors = ticketColors,
                    selectedColorIndex = selectedColorIndex,
                    onColorClick = { selectedColorIndex = it }
                )

                OutlinedTextField(
                    value = descriptionFieldValue,
                    onValueChange = { descriptionFieldValue = it },
                    label = { Text(text = stringResource(R.string.description_optional)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = assigneeFieldValue,
                    onValueChange = { assigneeFieldValue = it },
                    label = { Text(text = stringResource(R.string.assignee)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            showBoardMembers = focusState.hasFocus
                        }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = showBoardMembers,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    boardMembersUiState.Show()
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.create_ticket))
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
private fun CreateTicketScreenPreview() {
    KanbanBoardsTheme {
        CreateTicketScreen(
            navigateUp = {}
        )
    }
}