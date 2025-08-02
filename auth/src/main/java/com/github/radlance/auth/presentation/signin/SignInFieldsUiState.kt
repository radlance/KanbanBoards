package com.github.radlance.auth.presentation.signin

import com.github.radlance.auth.presentation.common.BaseFieldsUiState

data class SignInFieldsUiState(
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState