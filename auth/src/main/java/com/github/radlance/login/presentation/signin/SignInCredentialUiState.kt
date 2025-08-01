package com.github.radlance.login.presentation.signin

import androidx.compose.runtime.Composable
import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.presentation.AbstractUnitUiState
import com.github.radlance.common.presentation.ErrorMessage
import com.github.radlance.common.presentation.UnitUiState

interface SignInCredentialUiState : UnitUiState {

    @Composable
    fun Show(action: SignInCredentialAction)

    data class Success(
        private val idToken: String
    ) : SignInCredentialUiState, AbstractUnitUiState(hasSize = false) {

        @Composable
        override fun Show(action: SignInCredentialAction) = action.signInWithToken(idToken)
    }

    data class Error(
        private val manageResource: ManageResource
    ) : SignInCredentialUiState, AbstractUnitUiState(hasSize = true) {

        @Composable
        override fun Show(action: SignInCredentialAction) {
            ErrorMessage(
                message = manageResource.string(
                    com.github.radlance.common.R.string.error_enter_with_google
                )
            )
        }
    }

    object Initial : SignInCredentialUiState, AbstractUnitUiState(hasSize = false) {

        @Composable
        override fun Show(action: SignInCredentialAction) = Unit
    }
}