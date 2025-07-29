package com.github.radlance.kanbanboards.profile.presentation

import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

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
            ErrorMessage(message = manageResource.string(R.string.error_enter_with_google))
        }
    }

    object Initial : ProfileCredentialUiState {

        @Composable
        override fun Show(profileCredentialAction: ProfileCredentialAction) = Unit
    }
}