package com.github.radlance.kanbanboards.auth.presentation.signin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.presentation.AbstractUnitUiState
import com.github.radlance.kanbanboards.common.presentation.UnitUiState

interface CredentialUiState : UnitUiState {

    @Composable
    fun Show(action: SignInCredentialAction)

    data class Success(
        private val idToken: String
    ) : CredentialUiState, AbstractUnitUiState(hasSize = false, buttonEnabled = true) {

        @Composable
        override fun Show(action: SignInCredentialAction) = action.signInWithToken(idToken)
    }

    data class Error(
        private val manageResource: ManageResource
    ) : CredentialUiState, AbstractUnitUiState(hasSize = true, buttonEnabled = true) {

        @Composable
        override fun Show(action: SignInCredentialAction) {
            Text(
                text = manageResource.string(R.string.error_enter_with_google),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    object Initial : CredentialUiState, AbstractUnitUiState(hasSize = false, buttonEnabled = true) {

        @Composable
        override fun Show(action: SignInCredentialAction) = Unit
    }
}