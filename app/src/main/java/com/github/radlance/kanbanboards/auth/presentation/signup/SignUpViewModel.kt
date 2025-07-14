package com.github.radlance.kanbanboards.auth.presentation.signup

import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.domain.SignUpRepository
import com.github.radlance.kanbanboards.auth.presentation.common.BaseAuthViewModel
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateAuth
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val handleSignUp: HandleSignUp,
    private val authMapper: AuthResult.Mapper<AuthResultUiState>,
    private val validateAuth: ValidateAuth,
    runAsync: RunAsync
) : BaseAuthViewModel(handleSignUp, runAsync) {

    private val fieldsUiStateMutable = handleSignUp.fieldsState()

    val fieldsUiState get() = fieldsUiStateMutable.asStateFlow()

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {
        fieldsUiStateMutable.update {
            with(validateAuth) {
                it.copy(
                    nameErrorMessage = validName(name),
                    emailErrorMessage = validEmail(email),
                    passwordErrorMessage = validPassword(password),
                    confirmPasswordErrorMessage = validPasswordConfirm(confirmPassword, password)
                )
            }
        }

        with(fieldsUiState.value) {
            if (
                nameErrorMessage.isEmpty()
                && emailErrorMessage.isEmpty()
                && passwordErrorMessage.isEmpty()
                && confirmPasswordErrorMessage.isEmpty()
            ) {
                handleSignUp.saveAuthState(AuthResultUiState.Loading)

                handle(background = {
                    signUpRepository.signUp(
                        name = name,
                        email = email,
                        password = password
                    )
                }) { result ->
                    handleSignUp.saveAuthState(result.map(authMapper))
                }
            }
        }
    }

    override fun resetEmailError() = fieldsUiStateMutable.resetEmailError()

    override fun resetPasswordError() = fieldsUiStateMutable.resetPasswordError()

    fun resetNameError() {
        fieldsUiStateMutable.update { it.copy(nameErrorMessage = "") }
    }

    fun resetConfirmPasswordError() {
        fieldsUiStateMutable.update { it.copy(confirmPasswordErrorMessage = "") }
    }
}