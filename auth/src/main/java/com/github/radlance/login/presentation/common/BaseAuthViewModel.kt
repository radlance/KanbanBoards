package com.github.radlance.login.presentation.common

import com.github.radlance.common.presentation.BaseViewModel
import com.github.radlance.common.presentation.RunAsync
import com.github.radlance.login.presentation.signin.SignInFieldsUiState
import com.github.radlance.login.presentation.signup.SignUpFieldsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseAuthViewModel(
    baseHandle: BaseHandle,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    protected inline fun <reified T : BaseFieldsUiState> MutableStateFlow<T>.resetEmailError() {
        update { state ->
            when (state) {
                is SignInFieldsUiState -> state.copy(emailErrorMessage = "") as T
                is SignUpFieldsUiState -> state.copy(emailErrorMessage = "") as T
                else -> {
                    throw IllegalStateException("unknown type: ${T::class}")
                }
            }
        }
    }

    protected inline fun <reified T : BaseFieldsUiState> MutableStateFlow<T>.resetPasswordError() {
        update { state ->
            when (state) {
                is SignInFieldsUiState -> state.copy(passwordErrorMessage = "") as T
                is SignUpFieldsUiState -> state.copy(passwordErrorMessage = "") as T
                else -> {
                    throw IllegalStateException("unknown type: ${T::class}")
                }
            }
        }
    }

    val authResultUiState = baseHandle.authState

    abstract fun resetEmailError()

    abstract fun resetPasswordError()
}