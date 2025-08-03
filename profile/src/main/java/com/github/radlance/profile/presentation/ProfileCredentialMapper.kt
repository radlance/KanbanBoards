package com.github.radlance.profile.presentation

import com.github.radlance.auth.presentation.signin.CredentialResult
import com.github.radlance.core.core.ManageResource
import javax.inject.Inject

internal class ProfileCredentialMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<ProfileCredentialUiState> {

    override fun mapSuccess(idToken: String) = ProfileCredentialUiState.Success(idToken)

    override fun mapError() = ProfileCredentialUiState.Error(manageResource)
}