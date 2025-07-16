package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember

interface BoardMembersUiState {

    @Composable
    fun Show(modifier: Modifier = Modifier)

    data class Success(private val members: List<BoardMember>) : BoardMembersUiState {

        @Composable
        override fun Show(modifier: Modifier) {

            CreateTicketContent(
                members = members,
                modifier = modifier
            )
        }
    }

    data class Error(private val message: String) : BoardMembersUiState {
        @Composable
        override fun Show(modifier: Modifier) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    object Loading : BoardMembersUiState {

        @Composable
        override fun Show(modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}