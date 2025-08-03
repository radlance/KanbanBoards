package com.github.radlance.kanbanboards.auth.presentation

import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.BaseHandleSignIn
import com.github.radlance.kanbanboards.auth.presentation.signin.HandleSignIn
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInCredentialUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInFieldsUiState
import com.github.radlance.kanbanboards.core.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleSignInTest : BaseTest() {

    private lateinit var handleSignIn: HandleSignIn

    @Before
    fun setup() {
        handleSignIn = BaseHandleSignIn()
    }

    @Test
    fun test_sign_in_state() {
        assertEquals(AuthResultUiState.Initial, handleSignIn.authState.value)
        handleSignIn.saveAuthState(AuthResultUiState.Loading)
        assertEquals(AuthResultUiState.Loading, handleSignIn.authState.value)
        handleSignIn.saveAuthState(AuthResultUiState.Success)
        assertEquals(AuthResultUiState.Success, handleSignIn.authState.value)
        handleSignIn.saveAuthState(AuthResultUiState.Error(message = "test message"))
        assertEquals(
            AuthResultUiState.Error(message = "test message"),
            handleSignIn.authState.value
        )
    }

    @Test
    fun test_credential_state() {
        val manageResource = TestManageResource()

        assertEquals(SignInCredentialUiState.Initial, handleSignIn.credentialState.value)
        handleSignIn.saveCredentialState(SignInCredentialUiState.Success(idToken = "123456789"))

        assertEquals(
            SignInCredentialUiState.Success(idToken = "123456789"),
            handleSignIn.credentialState.value
        )

        handleSignIn.saveCredentialState(
            SignInCredentialUiState.Error(manageResource = manageResource)
        )

        assertEquals(
            SignInCredentialUiState.Error(manageResource = manageResource),
            handleSignIn.credentialState.value
        )
    }

    @Test
    fun test_fields_initial_state() {
        assertEquals(SignInFieldsUiState(), handleSignIn.fieldsState.value)
    }
}