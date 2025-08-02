package com.github.radlance.auth.presentation.signup

import com.github.radlance.auth.presentation.common.BaseFieldsUiState

data class SignUpFieldsUiState(
    val nameErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState
