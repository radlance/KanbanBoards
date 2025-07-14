package com.github.radlance.kanbanboards.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BaseColumn

@Composable
fun SignUpScreen(
    navigateToBoardsScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val fieldsUiState by viewModel.fieldsUiState.collectAsStateWithLifecycle()
    val signUpResultUiState by viewModel.authResultUiState.collectAsStateWithLifecycle()

    var nameFieldValue by rememberSaveable { mutableStateOf("") }
    var emailFieldValue by rememberSaveable { mutableStateOf("") }
    var passwordFieldValue by rememberSaveable { mutableStateOf("") }
    var passwordConfirmFieldValue by rememberSaveable { mutableStateOf("") }

    BaseColumn(modifier = modifier.safeDrawingPadding()) {
        Spacer(Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.for_the_first_time),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.enter_your_credentials),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))


            SignUpFields(
                nameFieldValue = nameFieldValue,
                onNameValueChange = {
                    viewModel.resetNameError()
                    nameFieldValue = it
                },
                emailFieldValue = emailFieldValue,
                onEmailValueChange = {
                    viewModel.resetEmailError()
                    emailFieldValue = it
                },
                passwordFieldValue = passwordFieldValue,
                onPasswordValueChange = {
                    passwordFieldValue = it
                    viewModel.resetPasswordError()
                },
                passwordConfirmFieldValue = passwordConfirmFieldValue,
                onPasswordConfirmValueChange = {
                    passwordConfirmFieldValue = it
                    viewModel.resetConfirmPasswordError()
                },
                fieldsUiState = fieldsUiState
            )

            Spacer(Modifier.height(16.dp))
            Button(
                enabled = signUpResultUiState.buttonEnabled(),
                onClick = {
                    viewModel.signUp(
                        name = nameFieldValue,
                        email = emailFieldValue,
                        password = passwordFieldValue,
                        confirmPassword = passwordConfirmFieldValue
                    )
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.sign_up))
            }
        }


        Column(modifier = Modifier.weight(1f)) {
            Spacer(Modifier.weight(1f))
            signUpResultUiState.Show(navigateToBoardsScreen = navigateToBoardsScreen)
            Spacer(Modifier.weight(1f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(16.dp))
            Row {
                Text(text = stringResource(R.string.already_have_an_account))
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = navigateToSignInScreen
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}