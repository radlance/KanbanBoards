package com.github.radlance.kanbanboards.board.settings.presentation

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.presentation.BackButton
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface SettingsBoardUiState {

    @Composable
    fun Show(
        navigateUp: () -> Unit,
        navigateToBoardsScreen: () -> Unit,
        boardSettingsAction: BoardSettingsAction,
        modifier: Modifier = Modifier
    )

    data class Success(private val boardInfo: BoardInfo) : SettingsBoardUiState {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            val boardSettingsUiState by boardSettingsAction.boardSettingsUiState.collectAsStateWithLifecycle()

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Crossfade(targetState = boardInfo.name) { name ->
                                Text(
                                    text = stringResource(R.string.named_settings, name),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        navigationIcon = { BackButton(navigateUp) }
                    )
                },
                modifier = modifier
            ) { contentPadding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    boardSettingsUiState.Show(
                        boardInfo = boardInfo,
                        boardSettingsMembersAction = boardSettingsAction,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    data class Error(private val message: String) : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message = message)
            }
        }
    }

    object Loading : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    object NotExists : SettingsBoardUiState {

        @Composable
        override fun Show(
            navigateUp: () -> Unit,
            navigateToBoardsScreen: () -> Unit,
            boardSettingsAction: BoardSettingsAction,
            modifier: Modifier
        ) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                navigateToBoardsScreen()
                Toast.makeText(
                    context,
                    R.string.you_have_been_removed_from_board,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}