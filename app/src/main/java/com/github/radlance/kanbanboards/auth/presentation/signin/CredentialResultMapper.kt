package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.common.core.ManageResource
import javax.inject.Inject

class CredentialResultMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<CredentialUiState> {

    override fun mapSuccess(idToken: String): CredentialUiState = CredentialUiState.Success(idToken)

    override fun mapError(): CredentialUiState = CredentialUiState.Error(manageResource)
}