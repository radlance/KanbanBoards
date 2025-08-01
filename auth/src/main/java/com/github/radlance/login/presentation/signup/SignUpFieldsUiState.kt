package com.github.radlance.login.presentation.signup

import com.github.radlance.login.presentation.common.BaseFieldsUiState

data class SignUpFieldsUiState(
    val nameErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState
