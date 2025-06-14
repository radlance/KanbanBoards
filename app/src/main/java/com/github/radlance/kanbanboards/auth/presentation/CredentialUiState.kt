package com.github.radlance.kanbanboards.auth.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import java.io.Serializable

interface CredentialUiState : Serializable {

    @Composable
    fun Show(signInAction: SignInAction)

    class Success(private val idToken: String) : CredentialUiState {

        @Composable
        override fun Show(signInAction: SignInAction) = signInAction.signIn(idToken)
    }

    class Error(private val manageResource: ManageResource) : CredentialUiState {

        @Composable
        override fun Show(signInAction: SignInAction) {
            Text(
                text = manageResource.string(R.string.error_enter_with_google),
                color = Color.Red,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    object Initial : CredentialUiState {

        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(signInAction: SignInAction) = Unit
    }
}