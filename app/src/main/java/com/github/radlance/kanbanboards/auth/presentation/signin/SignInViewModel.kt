package com.github.radlance.kanbanboards.auth.presentation.signin

import com.github.radlance.kanbanboards.auth.domain.SignInRepository
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateSignIn
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val credentialMapper: CredentialResult.Mapper<CredentialUiState>,
    private val validateSignIn: ValidateSignIn,
    handleSignIn: HandleSignIn,
    authMapper: UnitResult.Mapper<AuthResultUiState>,
    runAsync: RunAsync
) : BaseSignInViewModel(authMapper, handleSignIn, runAsync), SignInCredentialAction {

    val credentialResultUiState = handleSignIn.credentialState

    private val fieldsUiStateMutable = handleSignIn.fieldsState

    val fieldsUiState get() = fieldsUiStateMutable.asStateFlow()

    override fun signInWithToken(userTokenId: String) {
        handleAuth { signInRepository.signInWithToken(userTokenId) }
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
                handleAuth { signInRepository.signInWithEmail(email, password) }
            }
        }
    }

    override fun resetEmailError() = fieldsUiStateMutable.resetEmailError()

    override fun resetPasswordError() = fieldsUiStateMutable.resetPasswordError()
}

interface SignInCredentialAction {

    fun signInWithToken(userTokenId: String)
}
