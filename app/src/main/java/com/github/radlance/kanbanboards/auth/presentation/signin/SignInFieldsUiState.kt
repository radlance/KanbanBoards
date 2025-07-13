package com.github.radlance.kanbanboards.auth.presentation.signin

import java.io.Serializable

data class SignInFieldsUiState(
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = ""
) : Serializable