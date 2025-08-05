package com.github.radlance.kanbanboards.ticket.core.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.core.domain.User
import com.github.radlance.kanbanboards.core.presentation.BaseColumn
import com.github.radlance.ticket.R
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    boardId: String,
    members: List<User>,
    ticketActions: TicketActions,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: String = "",
    initialTitleFieldValue: String = "",
    initialSelectedAssignedList: List<String> = emptyList(),
    initialDescriptionFieldValue: String = "",
    ticketId: String = "",
    column: Column = Column.Todo,
    creationDate: LocalDateTime = LocalDateTime.now(),
    @StringRes buttonLabelId: Int
) {

    val ticketColors = remember {
        listOf(
            TicketColor.Yellow,
            TicketColor.Orange,
            TicketColor.Purple,
            TicketColor.Red,
            TicketColor.Green,
            TicketColor.Blue
        ).map { it.hex() }
    }

    var selectedColorIndex by rememberSaveable {
        mutableIntStateOf(
            if (selectedColor.isEmpty()) 0 else ticketColors.indexOf(selectedColor)
        )
    }
    var titleFieldValue by rememberSaveable { mutableStateOf(initialTitleFieldValue) }
    val selectedAssignedIds = remember { initialSelectedAssignedList.toMutableStateList() }
    var descriptionFieldValue by rememberSaveable { mutableStateOf(initialDescriptionFieldValue) }

    var expanded by rememberSaveable { mutableStateOf(false) }
    val ticketUiState by ticketActions.ticketUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    if (expanded) {
        var searchFieldValue by rememberSaveable { mutableStateOf("") }
        val localAssignedIds = remember { selectedAssignedIds.toMutableStateList() }
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

            val filteredMembers = remember(members, searchFieldValue) {
                members.filter { it.email.contains(searchFieldValue, ignoreCase = true) }
            }.sortedBy { it.email }

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
                                contentDescription = stringResource(com.github.radlance.core.R.string.search_icon)
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(com.github.radlance.core.R.string.search)
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
                                if (localAssignedIds.contains(member.id)) {
                                    localAssignedIds.remove(member.id)
                                } else {
                                    localAssignedIds.add(member.id)
                                }
                            },
                            text = {
                                val color = if (localAssignedIds.contains(member.id)) {
                                    MaterialTheme.colorScheme.primary
                                } else LocalTextStyle.current.color

                                Text(
                                    text = member.email,
                                    fontSize = 16.sp,
                                    color = color,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )

                        if (index < filteredMembers.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(onClick = { expanded = false }) {
                        Text(
                            text = stringResource(com.github.radlance.core.R.string.cancel),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    TextButton(
                        onClick = {
                            selectedAssignedIds.clear()
                            selectedAssignedIds.addAll(localAssignedIds)
                            expanded = false
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.ok),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }

    BaseColumn(
        modifier = modifier,
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
                placeholder = { Text(text = stringResource(com.github.radlance.core.R.string.at_least_3_symbol)) },
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Start)
            ) {
                val stringId = if (members.none { selectedAssignedIds.contains(it.id) }) {
                    R.string.no_assigned_members_yet
                } else com.github.radlance.core.R.string.assigned_to

                Crossfade(stringId) {
                    Text(text = stringResource(it))
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { expanded = true }) {
                    Text(text = stringResource(R.string.edit))
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = (-10).dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                members.filter { selectedAssignedIds.contains(it.id) }.forEach {
                    Text(text = it.email, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    if (it != members.last()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        val boxModifier = if (
            (ticketUiState.hasSize
                    || ticketUiState.hasSize) &&
            (scrollState.canScrollForward
                    || scrollState.canScrollBackward)
        ) {
            Modifier.heightIn(min = 81.dp)
        } else {
            Modifier.weight(1f)
        }

        Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
            ticketUiState.Show(navigateUp)
        }

        Box(modifier = Modifier.safeDrawingPadding()) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    ticketActions.action(
                        ticketId = ticketId,
                        boardId = boardId,
                        title = titleFieldValue,
                        color = ticketColors[selectedColorIndex],
                        description = descriptionFieldValue,
                        assigneeIds = members.filter { selectedAssignedIds.contains(it.id) }
                            .map { it.id },
                        column = column,
                        creationDate = creationDate
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = titleFieldValue.length >= 3 && ticketUiState.buttonEnabled
            ) {
                Text(text = stringResource(buttonLabelId))
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}