package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.domain.SignInRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.presentation.common.BaseAuthViewModel
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateSignIn
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val handleSignIn: HandleSignIn,
    private val authMapper: AuthResult.Mapper<AuthResultUiState>,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    private val validateSignIn: ValidateSignIn,
    runAsync: RunAsync
) : BaseAuthViewModel(handleSignIn, runAsync), SignInCredentialAction {

    val credentialResultUiState = handleSignIn.credentialState()

    private val fieldsUiStateMutable = handleSignIn.fieldsState()

    val fieldsUiState get() = fieldsUiStateMutable.asStateFlow()

    override fun signInWithToken(userTokenId: String) {
        handleSignIn.saveCredentialState(CredentialUiState.Initial)
        handleSignIn.saveAuthState(AuthResultUiState.Loading)

        handle(background = { signInRepository.signInWithToken(userTokenId) }) { result ->
            handleSignIn.saveAuthState(result.map(authMapper))
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
                handleSignIn.saveAuthState(AuthResultUiState.Loading)

                handle(background = { signInRepository.signInWithEmail(email, password) }) { result ->
                    handleSignIn.saveAuthState(result.map(authMapper))
                }
            }
        }
    }

    override fun resetEmailError() = fieldsUiStateMutable.resetEmailError()

    override fun resetPasswordError() = fieldsUiStateMutable.resetPasswordError()
}

interface SignInCredentialAction {

    fun signInWithToken(userTokenId: String)
}
