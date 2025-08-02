package com.github.radlance.invitation.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.core.presentation.BackButton
import com.github.radlance.core.presentation.BaseColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InvitationViewModel = hiltViewModel()
) {
    val invitationsUiState by viewModel.invitationsUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },

                title = {
                    Text(text = stringResource(com.github.radlance.core.R.string.invitations))
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(modifier = modifier.padding(contentPadding)) {
            invitationsUiState.Show(columnScope = this@BaseColumn, invitationAction = viewModel)
        }
    }
}