package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navigateToBoardsScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var emailFieldValue by rememberSaveable { mutableStateOf("") }
    var passwordFieldValue by rememberSaveable { mutableStateOf("") }

    val activity = LocalActivity.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val googleAccountManager: AccountManager? = remember {
        activity?.let { AccountManager.Google(it, FormatNonce.DigestFold) }
    }

    val fieldsUiState by viewModel.fieldsUiState.collectAsStateWithLifecycle()
    val signInResultUiState by viewModel.authResultUiState.collectAsStateWithLifecycle()
    val credentialResultUiState by viewModel.credentialResultUiState.collectAsStateWithLifecycle()

    BaseColumn(modifier = modifier.safeDrawingPadding(), scrollState = scrollState) {
        Spacer(Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.welcome_back),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.enter_your_credentials),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            SignInFields(
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
                fieldsUiState = fieldsUiState
            )

            Spacer(Modifier.height(16.dp))
            Button(
                enabled = signInResultUiState.buttonEnabled || credentialResultUiState.buttonEnabled,
                onClick = {
                    viewModel.signInWithEmail(
                        email = emailFieldValue,
                        password = passwordFieldValue
                    )
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.sign_in))
            }

            Spacer(Modifier.height(16.dp))

            IconButton(
                enabled = signInResultUiState.buttonEnabled || credentialResultUiState.buttonEnabled,
                onClick = {
                    scope.launch {
                        googleAccountManager?.signIn()?.let { viewModel.createCredential(it) }
                    }
                    keyboardController?.hide()
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
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
        }

        val columnModifier = if (
            (signInResultUiState.hasSize
                    || credentialResultUiState.hasSize) &&
            (scrollState.canScrollForward
                    || scrollState.canScrollBackward)
        ) {
            Modifier.heightIn(min = 81.dp)
        } else {
            Modifier.weight(1f)
        }

        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            signInResultUiState.Show(navigateToBoardsScreen)
            credentialResultUiState.Show(viewModel)
            Spacer(Modifier.weight(1f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(16.dp))
            Row {
                Text(text = stringResource(R.string.don_t_have_an_account))
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = {
                            keyboardController?.hide()
                            navigateToSignUpScreen()
                        }
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
