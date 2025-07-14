package com.github.radlance.kanbanboards.auth.presentation.signup

import com.github.radlance.kanbanboards.auth.presentation.common.BaseFieldsUiState
import java.io.Serializable

data class SignUpFieldsUiState(
    val nameErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : Serializable, BaseFieldsUiState
