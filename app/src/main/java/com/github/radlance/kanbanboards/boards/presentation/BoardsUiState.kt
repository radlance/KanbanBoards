package com.github.radlance.kanbanboards.boards.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

interface BoardsUiState {

    @Composable
    fun Show()

    data class Success(private val boards: List<BoardUi>) : BoardsUiState {

        @Composable
        override fun Show() {
            Column {
                boards.forEach {
                    Crossfade(targetState = it) { boardUi ->
                        boardUi.Show()
                    }
                }
            }
        }
    }

    data class Error(private val message: String) : BoardsUiState {

        @Composable
        override fun Show() {
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
        override fun Show() {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}