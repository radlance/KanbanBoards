package com.github.radlance.kanbanboards.createboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R

@Composable
fun CreateBoardContent(
    columnScope: ColumnScope,
    enabled: Boolean,
    loading: Boolean,
    fieldErrorMessage: String,
    createErrorMessage: String,
    createBoardActions: CreateBoardActions
) = with(columnScope) {
    var boardNameFieldValue by rememberSaveable { mutableStateOf("") }
    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    val searchUsersUiState by createBoardActions.searchUsersUiState.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = boardNameFieldValue,
        onValueChange = {
            boardNameFieldValue = it
            createBoardActions.checkBoard(it)
        },
        singleLine = true,
        isError = fieldErrorMessage.isNotEmpty(),
        placeholder = { Text(text = stringResource(R.string.at_least_3_symbol)) },
        label = {
            Text(
                text = fieldErrorMessage.ifEmpty {
                    stringResource(R.string.board_name)
                }
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = searchFieldValue,
        onValueChange = { searchFieldValue = it },
        singleLine = true,
        placeholder = {
            Text(text = stringResource(R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.search_icon)
            )
        },
        label = {
            Text(text = stringResource(R.string.search_users))
        },
        modifier = Modifier.fillMaxWidth()
    )

    searchUsersUiState.Show(
        checkActions = createBoardActions,
        columnScope = this,
        searchFieldValue = searchFieldValue
    )

    if (createErrorMessage.isNotEmpty()) {
        Text(
            text = createErrorMessage,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.error
            )
        )
        Spacer(Modifier.weight(1f))
    }


    Box(modifier = Modifier.safeDrawingPadding()) {
        if (loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { createBoardActions.createBoard(boardNameFieldValue) },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.create))
            }
        }
    }
    Spacer(Modifier.height(10.dp))
}