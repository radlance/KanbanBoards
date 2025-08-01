package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.common.core.ManageResource
import javax.inject.Inject

class ProfileCredentialMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<ProfileCredentialUiState> {

    override fun mapSuccess(idToken: String) = ProfileCredentialUiState.Success(idToken)

    override fun mapError() = ProfileCredentialUiState.Error(manageResource)
}