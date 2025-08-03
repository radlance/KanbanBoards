package com.github.radlance.kanbanboards.profile.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.auth.presentation.signin.AccountManager
import com.github.radlance.kanbanboards.auth.presentation.signin.FormatNonce
import com.github.radlance.kanbanboards.auth.presentation.signin.GoogleAccountManager
import com.github.radlance.profile.R
import kotlinx.coroutines.launch

interface ProfileProviderUi {

    @Composable
    fun Show(profileProviderAction: ProfileProviderAction)

    object Email : ProfileProviderUi {

        @Composable
        override fun Show(profileProviderAction: ProfileProviderAction) {

            val keyboardController = LocalSoftwareKeyboardController.current
            var showPassword by rememberSaveable { mutableStateOf(false) }
            var emailFieldValue by rememberSaveable { mutableStateOf("") }
            var passwordFieldValue by rememberSaveable { mutableStateOf("") }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = emailFieldValue,
                    onValueChange = { emailFieldValue = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    label = {
                        Text(text = stringResource(com.github.radlance.core.R.string.email))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                com.github.radlance.core.R.string.enter_your_email
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                OutlinedTextField(
                    value = passwordFieldValue,
                    onValueChange = { passwordFieldValue = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    label = {
                        Text(text = stringResource(com.github.radlance.core.R.string.password))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                com.github.radlance.core.R.string.enter_your_password
                            )
                        )
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
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        keyboardController?.hide()
                        profileProviderAction.deleteProfile(emailFieldValue, passwordFieldValue)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.delete_profile))
                }
            }
        }
    }

    object Google : ProfileProviderUi {

        @Composable
        override fun Show(profileProviderAction: ProfileProviderAction) {
            val activity = LocalActivity.current
            val scope = rememberCoroutineScope()

            val googleAccountManager: AccountManager? = remember {
                activity?.let { GoogleAccountManager(it, FormatNonce.DigestFold) }
            }

            val profileCredentialUiState by profileProviderAction.profileCredentialUiState.collectAsStateWithLifecycle()

            Spacer(Modifier.height(16.dp))
            IconButton(
                onClick = {
                    scope.launch {
                        googleAccountManager?.signIn()?.let {
                            profileProviderAction.createCredential(it)
                        }
                    }
                },
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(com.github.radlance.auth.R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            profileCredentialUiState.Show(profileProviderAction)
        }
    }

    object Initial : ProfileProviderUi {

        @Composable
        override fun Show(profileProviderAction: ProfileProviderAction) = Unit
    }
}