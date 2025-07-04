package com.github.radlance.kanbanboards.login.presentation

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test

class SignInViewModelTest : BaseTest() {

    private lateinit var handle: TestHandleSignIn
    private lateinit var signInMapper: SignInResultMapper
    private lateinit var credentialMapper: CredentialResultMapper
    private lateinit var manageResource: TestManageResources
    private lateinit var authRepository: TestAuthRepository

    private lateinit var viewModel: SignInViewModel


    @Before
    fun setup() {
        authRepository = TestAuthRepository()
        handle = TestHandleSignIn()
        signInMapper = SignInResultMapper()
        manageResource = TestManageResources()
        credentialMapper = CredentialResultMapper(manageResource)

        viewModel = SignInViewModel(
            authRepository = authRepository,
            handleSignIn = handle,
            signInMapper = signInMapper,
            credentialMapper = credentialMapper,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(SignInResultUiState.Initial, viewModel.signInResultUiState.value)
        assertEquals(CredentialUiState.Initial, viewModel.credentialResultUiState.value)

        assertEquals(1, handle.signInStateCalledCount)
        assertEquals(1, handle.credentialStateCalledCount)
        assertEquals(emptyList<SignInResultUiState>(), handle.saveSignInStateCalledList)
        assertEquals(emptyList<CredentialUiState>(), handle.saveCredentialStateCalledList)
    }

    @Test
    fun test_sign_in() {
        authRepository.signInResult = AuthResult.Error(message = "no internet connection")
        assertEquals(SignInResultUiState.Initial, viewModel.signInResultUiState.value)
        viewModel.signIn(userTokenId = "1234567890")
        assertEquals(1, authRepository.signInCalledList.size)
        assertEquals(2, handle.saveSignInStateCalledList.size)
        assertEquals(SignInResultUiState.Loading, handle.saveSignInStateCalledList[0])
        assertEquals(
            SignInResultUiState.Error("no internet connection"),
            handle.saveSignInStateCalledList[1]
        )
        assertEquals(
            SignInResultUiState.Error("no internet connection"),
            viewModel.signInResultUiState.value
        )

        authRepository.signInResult = AuthResult.Success
        viewModel.signIn(userTokenId = "1234567890")

        assertEquals(2, authRepository.signInCalledList.size)
        assertEquals(2, authRepository.signInCalledList.size)
        assertEquals(AuthResult.Success, authRepository.signInResult)
        assertEquals(4, handle.saveSignInStateCalledList.size)
        assertEquals(SignInResultUiState.Loading, handle.saveSignInStateCalledList[2])
        assertEquals(SignInResultUiState.Success, handle.saveSignInStateCalledList[3])
        assertEquals(SignInResultUiState.Success, viewModel.signInResultUiState.value)
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

    private class TestAuthRepository : AuthRepository {

        val signInCalledList = mutableListOf<String>()
        var signInResult: AuthResult = AuthResult.Success

        override suspend fun signIn(userIdToken: String): AuthResult {
            signInCalledList.add(userIdToken)
            return signInResult
        }
    }

    private class TestHandleSignIn : HandleSignIn {

        private var signInResultUiState =
            MutableStateFlow<SignInResultUiState>(SignInResultUiState.Initial)
        var saveSignInStateCalledList = mutableListOf<SignInResultUiState>()
        var signInStateCalledCount = 0

        private var credentialUiState =
            MutableStateFlow<CredentialUiState>(CredentialUiState.Initial)
        var saveCredentialStateCalledList = mutableListOf<CredentialUiState>()
        var credentialStateCalledCount = 0

        override fun saveSignInState(signInResultUiState: SignInResultUiState) {
            saveSignInStateCalledList.add(signInResultUiState)
            this.signInResultUiState.value = signInResultUiState
        }

        override fun signInState(): StateFlow<SignInResultUiState> {
            signInStateCalledCount++
            return signInResultUiState
        }

        override fun saveCredentialState(credentialUiState: CredentialUiState) {
            saveCredentialStateCalledList.add(credentialUiState)
            this.credentialUiState.value = credentialUiState
        }

        override fun credentialState(): StateFlow<CredentialUiState> {
            credentialStateCalledCount++
            return credentialUiState
        }
    }

    private class TestManageResources : ManageResource {

        override fun string(id: Int): String = "string $id"
    }
}