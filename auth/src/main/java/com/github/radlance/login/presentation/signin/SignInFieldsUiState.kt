package com.github.radlance.login.presentation.signin

import com.github.radlance.login.presentation.common.BaseFieldsUiState

data class SignInFieldsUiState(
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState