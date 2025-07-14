package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import java.io.Serializable

interface CredentialUiState : Serializable {

    @Composable
    fun Show(action: SignInCredentialAction)

    data class Success(private val idToken: String) : CredentialUiState {

        @Composable
        override fun Show(action: SignInCredentialAction) = action.signInWithToken(idToken)
    }

    data class Error(private val manageResource: ManageResource) : CredentialUiState {

        @Composable
        override fun Show(action: SignInCredentialAction) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = manageResource.string(R.string.error_enter_with_google),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    object Initial : CredentialUiState {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(action: SignInCredentialAction) = Unit
    }
}