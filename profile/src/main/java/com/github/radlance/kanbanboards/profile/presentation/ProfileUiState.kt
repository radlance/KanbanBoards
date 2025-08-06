package com.github.radlance.kanbanboards.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage
import com.github.radlance.profile.R

interface ProfileUiState {

    @Composable
    fun Show()

    data class Base(private val name: String, private val email: String) : ProfileUiState {

        @Composable
        override fun Show() {
            Column {
                Text(
                    text = stringResource(R.string.my_name, name),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.my_email, email),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    data class Error(private val message: String) : ProfileUiState {

        @Composable
        override fun Show() = ErrorMessage(message)
    }

    object Loading : ProfileUiState {

        @Composable
        override fun Show() = CircularProgressIndicator()
    }
}