package com.github.radlance.login.presentation.signin

import com.github.radlance.common.core.ManageResource
import javax.inject.Inject

class SignInCredentialMapper @Inject constructor(
    private val manageResource: ManageResource
) : CredentialResult.Mapper<SignInCredentialUiState> {

    override fun mapSuccess(idToken: String): SignInCredentialUiState = SignInCredentialUiState.Success(idToken)

    override fun mapError(): SignInCredentialUiState = SignInCredentialUiState.Error(manageResource)
}