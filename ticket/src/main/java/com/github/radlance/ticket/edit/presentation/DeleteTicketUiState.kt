package com.github.radlance.ticket.edit.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

interface DeleteTicketUiState {

    @Composable
    fun Show()

    object Success : DeleteTicketUiState {

        @Composable
        override fun Show() = Unit
    }

    data class Error(private val message: String) : DeleteTicketUiState {

        @Composable
        override fun Show() {

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    object Initial : DeleteTicketUiState {

        @Composable
        override fun Show() = Unit
    }
}