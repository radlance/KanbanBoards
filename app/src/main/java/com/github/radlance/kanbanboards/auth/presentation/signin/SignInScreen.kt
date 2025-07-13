package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onSuccessSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var emailFieldValue by rememberSaveable { mutableStateOf("") }
    var passwordFieldValue by rememberSaveable { mutableStateOf("") }

    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    val googleAccountManager: AccountManager? = remember {
        activity?.let { AccountManager.Google(it, FormatNonce.DigestFold) }
    }

    val fieldsUiState by viewModel.fieldsUiState.collectAsStateWithLifecycle()
    val signInResultUiState by viewModel.signInResultUiState.collectAsStateWithLifecycle()
    val credentialResultUiState by viewModel.credentialResultUiState.collectAsStateWithLifecycle()

    BaseColumn(modifier = modifier.safeDrawingPadding()) {
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

            OutlinedTextField(
                value = emailFieldValue,
                onValueChange = {
                    viewModel.resetEmailError()
                    emailFieldValue = it
                },
                label = {
                    val label = if (
                        emailFieldValue.isEmpty() || fieldsUiState.emailErrorMessage.isEmpty()
                    ) stringResource(R.string.email) else fieldsUiState.emailErrorMessage

                    Text(text = label)
                },
                placeholder = { Text(text = stringResource(R.string.enter_your_email)) },
                isError = fieldsUiState.emailErrorMessage.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordFieldValue,
                onValueChange = {
                    passwordFieldValue = it
                    viewModel.resetPasswordError()
                },
                label = {
                    val label = if (
                        passwordFieldValue.isEmpty() || fieldsUiState.passwordErrorMessage.isEmpty()
                    ) stringResource(R.string.password) else fieldsUiState.passwordErrorMessage

                    Text(text = label)
                },
                placeholder = { Text(text = stringResource(R.string.enter_your_password)) },
                isError = fieldsUiState.passwordErrorMessage.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.signInWithEmail(
                        email = emailFieldValue,
                        password = passwordFieldValue
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.sign_in))
            }

            Spacer(Modifier.height(32.dp))

            IconButton(
                onClick = {
                    scope.launch {
                        googleAccountManager?.signIn()?.let { viewModel.createCredential(it) }
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
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
        }


        Column(modifier = Modifier.weight(1f)) {
            Spacer(Modifier.weight(1f))
            signInResultUiState.Show(onSuccessSignIn)
            credentialResultUiState.Show(viewModel)
            Spacer(Modifier.weight(1f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Row {
                Text(text = stringResource(R.string.don_t_have_an_account))
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
