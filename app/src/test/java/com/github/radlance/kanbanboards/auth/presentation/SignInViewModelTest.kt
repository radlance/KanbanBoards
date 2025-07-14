package com.github.radlance.kanbanboards.auth.presentation

import com.github.radlance.kanbanboards.auth.domain.SignInRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateAuth
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResultMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.HandleSignIn
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInFieldsUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInViewModel
import com.github.radlance.kanbanboards.common.BaseTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test

class SignInViewModelTest : BaseTest() {

    private lateinit var handle: TestHandleSignIn
    private lateinit var signInMapper: AuthResultMapper
    private lateinit var credentialMapper: CredentialResultMapper
    private lateinit var manageResource: TestManageResource
    private lateinit var authRepository: TestSignInRepository
    private lateinit var validateAuth: TestValidateAuth

    private lateinit var viewModel: SignInViewModel


    @Before
    fun setup() {
        authRepository = TestSignInRepository()
        handle = TestHandleSignIn()
        signInMapper = AuthResultMapper()
        manageResource = TestManageResource()
        credentialMapper = CredentialResultMapper(manageResource)
        validateAuth = TestValidateAuth()

        viewModel = SignInViewModel(
            signInRepository = authRepository,
            handleSignIn = handle,
            authMapper = signInMapper,
            credentialMapper = credentialMapper,
            runAsync = TestRunAsync(),
            validateSignIn = validateAuth
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(AuthResultUiState.Initial, viewModel.authResultUiState.value)
        assertEquals(CredentialUiState.Initial, viewModel.credentialResultUiState.value)
        assertEquals(SignInFieldsUiState(), viewModel.fieldsUiState.value)

        assertEquals(1, handle.signInStateCalledCount)
        assertEquals(1, handle.credentialStateCalledCount)
        assertEquals(1, handle.fieldsStateCalledCount)
        assertEquals(emptyList<AuthResultUiState>(), handle.saveSignInStateCalledList)
        assertEquals(emptyList<CredentialUiState>(), handle.saveCredentialStateCalledList)
    }

    @Test
    fun test_sign_in_with_token() {
        authRepository.signInWithTokenResult = AuthResult.Error(message = "no internet connection")
        viewModel.signInWithToken(userTokenId = "1234567890")
        assertEquals(1, authRepository.signInWithTokenCalledList.size)
        assertEquals("1234567890", authRepository.signInWithTokenCalledList[0])
        assertEquals(2, handle.saveSignInStateCalledList.size)
        assertEquals(AuthResultUiState.Loading, handle.saveSignInStateCalledList[0])
        assertEquals(
            AuthResultUiState.Error("no internet connection"),
            handle.saveSignInStateCalledList[1]
        )
        assertEquals(
            AuthResultUiState.Error("no internet connection"),
            viewModel.authResultUiState.value
        )

        authRepository.signInWithTokenResult = AuthResult.Success
        viewModel.signInWithToken(userTokenId = "1234567890")

        assertEquals(2, authRepository.signInWithTokenCalledList.size)
        assertEquals("1234567890", authRepository.signInWithTokenCalledList[1])
        assertEquals(4, handle.saveSignInStateCalledList.size)
        assertEquals(AuthResultUiState.Loading, handle.saveSignInStateCalledList[2])
        assertEquals(AuthResultUiState.Success, handle.saveSignInStateCalledList[3])
        assertEquals(AuthResultUiState.Success, viewModel.authResultUiState.value)
    }

    @Test
    fun test_create_credential() {
        viewModel.createCredential(CredentialResult.Error)
        assertEquals(1, handle.saveCredentialStateCalledList.size)
        assertEquals(
            CredentialUiState.Error(manageResource),
            handle.saveCredentialStateCalledList[0]
        )

        viewModel.createCredential(CredentialResult.Success(idToken = "987654321"))
        assertEquals(2, handle.saveCredentialStateCalledList.size)
        assertEquals(
            CredentialUiState.Success(idToken = "987654321"),
            handle.saveCredentialStateCalledList[1]
        )
    }

    @Test
    fun test_sign_in_with_email_invalid_email() {
        viewModel.signInWithEmail(email = "", password = "123456")
        assertEquals(0, authRepository.signInWithEmailCalledList.size)
        assertEquals(0, handle.saveSignInStateCalledList.size)
        assertEquals(
            SignInFieldsUiState(emailErrorMessage = "invalid email", passwordErrorMessage = ""),
            viewModel.fieldsUiState.value
        )
    }

    @Test
    fun test_sign_in_invalid_password() {
        viewModel.signInWithEmail(email = "test@email.com", password = "")
        assertEquals(0, authRepository.signInWithEmailCalledList.size)
        assertEquals(0, handle.saveSignInStateCalledList.size)
        assertEquals(
            SignInFieldsUiState(emailErrorMessage = "", passwordErrorMessage = "invalid password"),
            viewModel.fieldsUiState.value
        )
    }

    @Test
    fun test_sign_in() {
        authRepository.signInWithEmailResult = AuthResult.Error(message = "server error")
        viewModel.signInWithEmail(email = "test@email.com", password = "123456")
        assertEquals(1, authRepository.signInWithEmailCalledList.size)
        assertEquals(Pair("test@email.com", "123456"), authRepository.signInWithEmailCalledList[0])
        assertEquals(2, handle.saveSignInStateCalledList.size)
        assertEquals(AuthResultUiState.Loading, handle.saveSignInStateCalledList[0])
        assertEquals(
            AuthResultUiState.Error("server error"),
            handle.saveSignInStateCalledList[1]
        )
        assertEquals(
            AuthResultUiState.Error("server error"),
            viewModel.authResultUiState.value
        )

        authRepository.signInWithEmailResult = AuthResult.Success

        viewModel.signInWithEmail(email = "test@email.com", password = "123456")
        assertEquals(2, authRepository.signInWithEmailCalledList.size)
        assertEquals(Pair("test@email.com", "123456"), authRepository.signInWithEmailCalledList[1])
        assertEquals(4, handle.saveSignInStateCalledList.size)
        assertEquals(AuthResultUiState.Loading, handle.saveSignInStateCalledList[2])
        assertEquals(AuthResultUiState.Success, handle.saveSignInStateCalledList[3])
        assertEquals(AuthResultUiState.Success, viewModel.authResultUiState.value)
    }

    @Test
    fun reset_errors_test() {
        viewModel.signInWithEmail(email = "", password = "")
        assertEquals(
            SignInFieldsUiState(
                emailErrorMessage = "invalid email",
                passwordErrorMessage = "invalid password"
            ),
            viewModel.fieldsUiState.value
        )

        viewModel.resetEmailError()

        assertEquals(
            SignInFieldsUiState(emailErrorMessage = "", passwordErrorMessage = "invalid password"),
            viewModel.fieldsUiState.value
        )

        viewModel.resetPasswordError()

        assertEquals(
            SignInFieldsUiState(emailErrorMessage = "", passwordErrorMessage = ""),
            viewModel.fieldsUiState.value
        )
    }

    private class TestSignInRepository : SignInRepository {

        val signInWithTokenCalledList = mutableListOf<String>()
        var signInWithTokenResult: AuthResult = AuthResult.Success

        val signInWithEmailCalledList = mutableListOf<Pair<String, String>>()
        var signInWithEmailResult: AuthResult = AuthResult.Success

        override suspend fun signInWithToken(userIdToken: String): AuthResult {
            signInWithTokenCalledList.add(userIdToken)
            return signInWithTokenResult
        }

        override suspend fun signInWithEmail(email: String, password: String): AuthResult {
            signInWithEmailCalledList.add(Pair(email, password))
            return signInWithEmailResult
        }
    }

    private class TestHandleSignIn : HandleSignIn {

        private var authResultUiState =
            MutableStateFlow<AuthResultUiState>(AuthResultUiState.Initial)
        var saveSignInStateCalledList = mutableListOf<AuthResultUiState>()
        var signInStateCalledCount = 0

        private var credentialUiState =
            MutableStateFlow<CredentialUiState>(CredentialUiState.Initial)
        var saveCredentialStateCalledList = mutableListOf<CredentialUiState>()
        var credentialStateCalledCount = 0

        private var fieldsUiState = MutableStateFlow(SignInFieldsUiState())
        var fieldsStateCalledCount = 0

        override fun saveAuthState(authResultUiState: AuthResultUiState) {
            saveSignInStateCalledList.add(authResultUiState)
            this.authResultUiState.value = authResultUiState
        }

        override fun authState(): StateFlow<AuthResultUiState> {
            signInStateCalledCount++
            return authResultUiState
        }

        override fun saveCredentialState(credentialUiState: CredentialUiState) {
            saveCredentialStateCalledList.add(credentialUiState)
            this.credentialUiState.value = credentialUiState
        }

        override fun credentialState(): StateFlow<CredentialUiState> {
            credentialStateCalledCount++
            return credentialUiState
        }

        override fun fieldsState(): MutableStateFlow<SignInFieldsUiState> {
            fieldsStateCalledCount++
            return fieldsUiState
        }
    }

    private class TestValidateAuth : ValidateAuth {
        private val validNameCalledList = mutableListOf<String>()
        private val validPasswordConfirmCalledList = mutableListOf<Pair<String, String>>()
        private val validEmailCalledList = mutableListOf<String>()
        private val validPasswordCalledList = mutableListOf<String>()

        override fun validName(value: String): String {
            validNameCalledList.add(value)
            return if (value.isBlank()) "invalid name" else ""
        }

        override fun validPasswordConfirm(confirm: String, password: String): String {
            validPasswordConfirmCalledList.add(Pair(password, confirm))
            return if (password.isBlank() && confirm.isBlank()) "invalid confirm" else ""
        }

        override fun validEmail(value: String): String {
            validEmailCalledList.add(value)
            return if (value.isBlank()) "invalid email" else ""
        }

        override fun validPassword(value: String): String {
            validPasswordCalledList.add(value)
            return if (value.isBlank()) "invalid password" else ""
        }
    }
}