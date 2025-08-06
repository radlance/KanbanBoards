package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.core.ManageResource
import javax.inject.Inject

internal class ProfileCredentialMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<ProfileCredentialUiState> {

    override fun mapSuccess(idToken: String) = ProfileCredentialUiState.Success(idToken)

    override fun mapError() = ProfileCredentialUiState.Error(manageResource)
}