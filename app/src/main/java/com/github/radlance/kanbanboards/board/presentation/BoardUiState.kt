package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import java.io.Serializable

interface BoardUiState : Serializable {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        ticketActions: TicketActions,
        modifier: Modifier = Modifier
    )

    data class Success(private val boardInfo: BoardInfo) : BoardUiState {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            ticketActions: TicketActions,
            modifier: Modifier
        ) {
            val ticketUiState by ticketActions.ticketUiState.collectAsStateWithLifecycle()

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Crossfade(targetState = boardInfo.name) { name ->
                                Text(text = name, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = navigateUp) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.navigate_up)
                                )
                            }
                        },
                        actions = {
                            if (boardInfo.isMyBoard) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = stringResource(R.string.settings_icon)
                                )
                            }
                        }
                    )
                },
                modifier = modifier
            ) { contentPadding ->
                BaseColumn(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(contentPadding)
                ) {
                    ticketUiState.Show()
                }
            }
        }
    }

    data class Error(private val message: String) : BoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            ticketActions: TicketActions,
            modifier: Modifier
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }

    object Loading : BoardUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            ticketActions: TicketActions,
            modifier: Modifier
        ) {
            CircularProgressIndicator()
        }
    }
}