package com.github.radlance.kanbanboards.auth.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.radlance.kanbanboards.R
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onSuccessSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    val googleAccountManager: AccountManager? = remember {
        activity?.let { AccountManager.Google(it, FormatNonce.DigestFold) }
    }

    val signInResultUiState by viewModel.signInResultUiState.collectAsStateWithLifecycle()
    val credentialResultUiState by viewModel.credentialResultUiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(R.string.welcome_to_kanban_boards),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))

            signInResultUiState.Handle(onSuccessSignIn)
            credentialResultUiState.Show(viewModel)

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        googleAccountManager?.signIn()?.let { viewModel.createCredential(it) }
                    }
                }
            ) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = "Google"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.sign_in_with_google))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}