package com.github.radlance.kanbanboards.boards.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BaseColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(
    navigateToProfile: () -> Unit,
    navigateToBoardCreation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BoardsViewModel = hiltViewModel()
) {
    val boardsUiState by viewModel.boards.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.boards))
                },
                actions = {
                    IconButton(onClick = navigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.account_icon)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(contentPadding)
        ) {
            boardsUiState.Show(
                columnScope = this@BaseColumn,
                navigateToBoardCreation = navigateToBoardCreation
            )
        }
    }
}