package com.github.radlance.kanbanboards.board.create.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.presentation.BackButton
import com.github.radlance.kanbanboards.common.presentation.BaseColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBoardsScreen(
    navigateUp: () -> Unit,
    navigateToBoardScreen: (BoardInfo) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateBoardViewModel = hiltViewModel()
) {
    val createBoardUiState by viewModel.createBoardUiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { viewModel.resetBoardUiState() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },
                title = {
                    Text(text = stringResource(R.string.create_board))
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            createBoardUiState.Show(
                navigateToBoardScreen = navigateToBoardScreen,
                columnScope = this@BaseColumn,
                createBoardActions = viewModel
            )
        }
    }
}