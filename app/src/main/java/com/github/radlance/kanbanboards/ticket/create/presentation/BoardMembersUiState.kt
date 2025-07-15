package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import java.io.Serializable

interface BoardMembersUiState : Serializable {

    @Composable
    fun Show()

    data class Success(private val members: List<BoardMember>) : BoardMembersUiState {

        @Composable
        override fun Show() {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column {
                        Spacer(Modifier.height(8.dp))
                        Text(text = "Board members")
                    }
                }

                items(items = members, key = { it.id }) { member ->
                    Crossfade(targetState = member) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = it.email,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    data class Error(private val message: String) : BoardMembersUiState {
        @Composable
        override fun Show() {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    object Loading : BoardMembersUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show() = CircularProgressIndicator()
    }
}