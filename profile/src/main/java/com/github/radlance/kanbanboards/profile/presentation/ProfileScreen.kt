package com.github.radlance.kanbanboards.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.core.presentation.BackButton
import com.github.radlance.kanbanboards.core.presentation.BaseColumn
import com.github.radlance.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateToSignIn: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

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
                    IconButton(onClick = { navigateToEditProfile() }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = stringResource(R.string.edit_profile)
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
                    navigateToSignIn()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(com.github.radlance.core.R.string.sign_out))
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}