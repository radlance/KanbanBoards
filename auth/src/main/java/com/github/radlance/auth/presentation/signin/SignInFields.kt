package com.github.radlance.auth.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
internal fun SignInFields(
    emailFieldValue: String,
    onEmailValueChange: (String) -> Unit,
    passwordFieldValue: String,
    onPasswordValueChange: (String) -> Unit,
    fieldsUiState: SignInFieldsUiState,
    modifier: Modifier = Modifier
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        OutlinedTextField(
            value = emailFieldValue,
            onValueChange = onEmailValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            label = {
                val label = if (
                    emailFieldValue.isEmpty() || fieldsUiState.emailErrorMessage.isEmpty()
                ) stringResource(com.github.radlance.core.R.string.email) else {
                    fieldsUiState.emailErrorMessage
                }

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(com.github.radlance.core.R.string.enter_your_email))
            },
            isError = fieldsUiState.emailErrorMessage.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = passwordFieldValue,
            onValueChange = onPasswordValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            label = {
                val label = if (
                    passwordFieldValue.isEmpty() || fieldsUiState.passwordErrorMessage.isEmpty()
                ) stringResource(com.github.radlance.core.R.string.password) else {
                    fieldsUiState.passwordErrorMessage
                }

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(com.github.radlance.core.R.string.enter_your_password))
            },
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (showPassword) {
                    Icons.Default.VisibilityOff
                } else Icons.Default.Visibility

                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(
                            com.github.radlance.core.R.string.show_hide_password_icon
                        )
                    )
                }
            },
            isError = fieldsUiState.passwordErrorMessage.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}