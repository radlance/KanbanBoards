package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.presentation.BackButton
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface TicketInfoUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Success(private val ticket: Ticket) : TicketInfoUiState {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            BackButton(navigateUp)
                        },
                        title = {
                            Text(text = ticket.name)
                        }
                    )
                },
                modifier = modifier
            ) { contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = ticket.toString())
                }
            }
        }
    }

    data class Error(private val message: String) : TicketInfoUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : TicketInfoUiState {

        @Composable
        override fun Show(navigateUp: () -> Unit, modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}