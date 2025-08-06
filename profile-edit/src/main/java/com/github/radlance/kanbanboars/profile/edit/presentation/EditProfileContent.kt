package com.github.radlance.kanbanboars.profile.edit.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboars.profile.edit.R

@Composable
fun EditProfileContent(
    columnScope: ColumnScope,
    scrollState: ScrollState,
    currentName: String,
    editProfileAction: EditProfileAction,
    navigateUp: () -> Unit,
) = with(columnScope) {

    val editProfileUiState by editProfileAction.editProfileUiState.collectAsStateWithLifecycle()

    var nameFieldValue by rememberSaveable { mutableStateOf(currentName) }


    OutlinedTextField(
        value = nameFieldValue,
        onValueChange = { nameFieldValue = it },
        singleLine = true,
        label = { Text(text = stringResource(com.github.radlance.core.R.string.name)) },
        placeholder = { Text(text = stringResource(R.string.enter_new_name)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))

    val columnModifier = if (
        editProfileUiState.hasSize
        && (scrollState.canScrollForward || scrollState.canScrollBackward)
    ) {
        Modifier.heightIn(min = 81.dp)
    } else {
        Modifier.weight(1f)
    }

    Column(modifier = columnModifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        editProfileUiState.Show(
            navigateUp = {
                navigateUp()
                editProfileAction.resetEditProfileUiState()
            }
        )
        Spacer(Modifier.weight(1f))
    }

    Button(
        onClick = { editProfileAction.editProfile(nameFieldValue) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.edit_profile))
    }
    Spacer(Modifier.height(10.dp))
}