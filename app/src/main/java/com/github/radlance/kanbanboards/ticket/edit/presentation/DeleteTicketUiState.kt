package com.github.radlance.kanbanboards.ticket.edit.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

interface DeleteTicketUiState {

    @Composable
    fun Show(navigateToBoard: () -> Unit)

    object Success : DeleteTicketUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit) {
            navigateToBoard()
        }
    }

    data class Error(private val message: String) : DeleteTicketUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit) {

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    object Initial : DeleteTicketUiState {

        @Composable
        override fun Show(navigateToBoard: () -> Unit) = Unit
    }
}