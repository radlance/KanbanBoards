package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketContent(
    boardId: String,
    members: List<BoardMember>,
    ticketActions: TicketActions,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedColorIndex by rememberSaveable { mutableIntStateOf(0) }
    var titleFieldValue by rememberSaveable { mutableStateOf("") }
    var selectedAssigneeId by rememberSaveable { mutableStateOf("") }
    var descriptionFieldValue by rememberSaveable { mutableStateOf("") }

    var expanded by rememberSaveable { mutableStateOf(false) }
    val createTicketUiState by ticketActions.createTicketUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

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

    if (expanded) {
        var searchFieldValue by rememberSaveable { mutableStateOf("") }
        BasicAlertDialog(
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .clip(AlertDialogDefaults.shape)
                .heightIn(max = 400.dp)
                .background(AlertDialogDefaults.containerColor)
        ) {
            val listState = rememberLazyListState()

            if (selectedAssigneeId != "") {
                val index = members.indexOf(members.first { it.id == selectedAssigneeId })
                LaunchedEffect(Unit) {
                    listState.scrollToItem(index)
                }
            }

            val filteredMembers = remember(members, searchFieldValue) {
                members.filter { it.email.contains(searchFieldValue, ignoreCase = true) }
            }

            Column(modifier = Modifier.padding(30.dp)) {
                AnimatedVisibility(members.size > 5) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = searchFieldValue,
                        onValueChange = { searchFieldValue = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.search_icon)
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search)
                            )
                        },
                        singleLine = true
                    )
                }
                Spacer(Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = listState,
                ) {
                    itemsIndexed(
                        items = filteredMembers, key = { _, member -> member.id }
                    ) { index, member ->
                        DropdownMenuItem(
                            onClick = {
                                selectedAssigneeId = member.id
                                expanded = false
                            },
                            text = {
                                val color by animateColorAsState(
                                    if (selectedAssigneeId == member.id) {
                                        MaterialTheme.colorScheme.primary
                                    } else LocalTextStyle.current.color
                                )
                                Text(text = member.email, fontSize = 16.sp, color = color)
                            }
                        )

                        if (index < filteredMembers.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }

    BaseColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollState = scrollState
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

            Box(modifier = Modifier.height(IntrinsicSize.Min)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    readOnly = true,
                    value = members.find { it.id == selectedAssigneeId }?.email ?: "",
                    onValueChange = {},
                    singleLine = true,
                    placeholder = { Text(text = stringResource(R.string.assignee)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { expanded = true },
                    color = Color.Transparent,
                ) {}
            }
        }

        val boxModifier = if (
            (createTicketUiState.hasSize()
                    || createTicketUiState.hasSize()) &&
            (scrollState.canScrollForward
                    || scrollState.canScrollBackward)
        ) {
            Modifier.heightIn(min = 81.dp)
        } else {
            Modifier.weight(1f)
        }

        Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
            createTicketUiState.Show(navigateUp)
        }

        Box(modifier = Modifier.safeDrawingPadding()) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    ticketActions.createTicket(
                        boardId = boardId,
                        title = titleFieldValue,
                        color = ticketColors[selectedColorIndex].hex(),
                        description = descriptionFieldValue,
                        assignee = members.find { it.id == selectedAssigneeId }?.id ?: ""
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = titleFieldValue.length >= 3 && createTicketUiState.buttonEnabled()
            ) {
                Text(text = stringResource(R.string.create_ticket))
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}