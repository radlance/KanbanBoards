package com.github.radlance.kanbanboars.profile.edit.presentation

import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage

interface ProfileCredentialUiState {

    @Composable
    fun Show(profileCredentialAction: ProfileCredentialAction)

    data class Success(
        private val idToken: String
    ) : ProfileCredentialUiState {

        @Composable
        override fun Show(profileCredentialAction: ProfileCredentialAction) {
            profileCredentialAction.deleteProfile(idToken)
        }
    }

    data class Error(private val manageResource: ManageResource) : ProfileCredentialUiState {

        @Composable
        override fun Show(profileCredentialAction: ProfileCredentialAction) {
            ErrorMessage(
                message = manageResource.string(
                    com.github.radlance.core.R.string.error_enter_with_google
                )
            )
        }
    }

    object Initial : ProfileCredentialUiState {

        @Composable
        override fun Show(profileCredentialAction: ProfileCredentialAction) = Unit
    }
}