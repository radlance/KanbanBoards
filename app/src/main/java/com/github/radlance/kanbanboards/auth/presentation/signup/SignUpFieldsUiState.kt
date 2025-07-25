package com.github.radlance.kanbanboards.auth.presentation.signup

import com.github.radlance.kanbanboards.auth.presentation.common.BaseFieldsUiState

data class SignUpFieldsUiState(
    val nameErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState
