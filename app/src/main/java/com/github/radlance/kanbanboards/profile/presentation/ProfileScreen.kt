package com.github.radlance.kanbanboards.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.core.presentation.BackButton
import com.github.radlance.core.presentation.BaseColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateToSignInScreen: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val deleteProfileUiState by viewModel.deleteProfileUiState.collectAsStateWithLifecycle()
    val profileProviderUi by viewModel.profileProviderUi.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            modifier = Modifier
                .clip(AlertDialogDefaults.shape)
                .heightIn(max = 400.dp)
                .background(AlertDialogDefaults.containerColor)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(30.dp)
            ) {
                Text(
                    text = stringResource(
                        com.github.radlance.core.R.string.sign_in_again_to_delete_your_profile
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                profileProviderUi.Show(viewModel)
                deleteProfileUiState.Show {
                    showDialog = false
                    navigateToSignInScreen()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },
                title = {
                    Text(text = stringResource(com.github.radlance.core.R.string.profile))
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(
                                com.github.radlance.core.R.string.delete_profile
                            )
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        BaseColumn(modifier = modifier.padding(contentPadding)) {
            Spacer(Modifier.weight(1f))
            profileUiState.Show()
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.signOut()
                    navigateToSignInScreen()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(com.github.radlance.core.R.string.sign_out))
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}