package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.presentation.common.BaseFieldsUiState

data class SignInFieldsUiState(
    override val emailErrorMessage: String = "",
    override val passwordErrorMessage: String = ""
) : BaseFieldsUiState