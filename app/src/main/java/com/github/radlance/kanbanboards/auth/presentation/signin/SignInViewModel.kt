package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateSignIn
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val handleSignIn: HandleSignIn,
    private val signInMapper: AuthResult.Mapper<SignInResultUiState>,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    private val validateSignIn: ValidateSignIn,
    runAsync: RunAsync
) : BaseViewModel(runAsync), SignInCredentialAction {

    val signInResultUiState = handleSignIn.signInState()

    val credentialResultUiState = handleSignIn.credentialState()

    private val fieldsUiStateMutable = handleSignIn.fieldsState()

    val fieldsUiState get() = fieldsUiStateMutable.asStateFlow()

    override fun signInWithToken(userTokenId: String) {
        handleSignIn.saveCredentialState(CredentialUiState.Initial)
        handleSignIn.saveSignInState(SignInResultUiState.Loading)

        handle(background = { authRepository.signInWithToken(userTokenId) }) { result ->
            handleSignIn.saveSignInState(result.map(signInMapper))
        }
    }

    fun createCredential(credentialResult: CredentialResult) {
        handleSignIn.saveCredentialState(credentialResult.map(credentialMapper))
    }

    fun signInWithEmail(email: String, password: String) {
        fieldsUiStateMutable.update {
            with(validateSignIn) {
                it.copy(
                    emailErrorMessage = validEmail(email),
                    passwordErrorMessage = validPassword(password)
                )
            }
        }

        with(fieldsUiState.value) {
            if (emailErrorMessage.isEmpty() && passwordErrorMessage.isEmpty()) {
                handleSignIn.saveSignInState(SignInResultUiState.Loading)

                handle(background = { authRepository.signInWithEmail(email, password) }) { result ->
                    handleSignIn.saveSignInState(result.map(signInMapper))
                }
            }
        }
    }

    fun resetEmailError() {
        fieldsUiStateMutable.update { it.copy(emailErrorMessage = "") }
    }

    fun resetPasswordError() {
        fieldsUiStateMutable.update { it.copy(passwordErrorMessage = "") }
    }
}

interface SignInCredentialAction {

    fun signInWithToken(userTokenId: String)
}
