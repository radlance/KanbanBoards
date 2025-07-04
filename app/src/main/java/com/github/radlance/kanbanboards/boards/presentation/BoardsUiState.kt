package com.github.radlance.kanbanboards.boards.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.R

interface BoardsUiState {

    @Composable
    fun Show(
        columnScope: ColumnScope,
        navigateToBoardCreation: () -> Unit,
        navigateToBoard: () -> Unit
    )

    data class Success(private val boards: List<BoardUi>) : BoardsUiState {

        @Composable
        override fun Show(
            columnScope: ColumnScope,
            navigateToBoardCreation: () -> Unit,
            navigateToBoard: () -> Unit
        ) = with(columnScope) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                boards.forEach {
                    Crossfade(targetState = it) { boardUi ->
                        boardUi.Show()
                    }
                }
            }

            Button(onClick = navigateToBoardCreation, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.create))
            }
            Spacer(Modifier.height(10.dp))
        }
    }

    data class Error(private val message: String) : BoardsUiState {

        @Composable
        override fun Show(
            columnScope: ColumnScope,
            navigateToBoardCreation: () -> Unit,
            navigateToBoard: () -> Unit
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }

    object Loading : BoardsUiState {

        @Composable
        override fun Show(
            columnScope: ColumnScope,
            navigateToBoardCreation: () -> Unit,
            navigateToBoard: () -> Unit
        ) = with(columnScope) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
    }
}