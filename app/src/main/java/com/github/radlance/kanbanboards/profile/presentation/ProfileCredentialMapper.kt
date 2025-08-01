package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.common.core.ManageResource
import com.github.radlance.login.presentation.signin.CredentialResult
import javax.inject.Inject

class ProfileCredentialMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<ProfileCredentialUiState> {

    override fun mapSuccess(idToken: String) = ProfileCredentialUiState.Success(idToken)

    override fun mapError() = ProfileCredentialUiState.Error(manageResource)
}