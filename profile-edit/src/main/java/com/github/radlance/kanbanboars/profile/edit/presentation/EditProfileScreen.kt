package com.github.radlance.kanbanboars.profile.edit.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
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
import com.github.radlance.kanbanboards.core.presentation.BackButton
import com.github.radlance.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navigateUp: () -> Unit,
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val profileProviderUi by viewModel.profileProviderUi.collectAsStateWithLifecycle()
    val deleteProfileUiState by viewModel.deleteProfileUiState.collectAsStateWithLifecycle()

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
                    text = stringResource(R.string.sign_in_again_to_delete_your_profile),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                profileProviderUi.Show(viewModel)
                deleteProfileUiState.Show {
                    showDialog = false
                    navigateToSignIn()
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
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete_profile)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(com.github.radlance.core.R.string.edit_profile))
                }
            )
        }
    ) { contentPadding ->
        profileUiState.Show(
            editProfileAction = viewModel,
            navigateUp = navigateUp,
            modifier = modifier.padding(contentPadding)
        )
    }
}