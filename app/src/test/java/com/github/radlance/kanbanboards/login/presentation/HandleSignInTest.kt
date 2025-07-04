package com.github.radlance.kanbanboards.login.presentation

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.common.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleSignInTest : BaseTest() {

    private lateinit var handleSignIn: HandleSignIn

    @Before
    fun setup() {
        handleSignIn = HandleSignIn.Base(SavedStateHandle())
    }

    @Test
    fun test_sign_in_state() {
        assertEquals(SignInResultUiState.Initial, handleSignIn.signInState().value)
        handleSignIn.saveSignInState(SignInResultUiState.Loading)
        assertEquals(SignInResultUiState.Loading, handleSignIn.signInState().value)
        handleSignIn.saveSignInState(SignInResultUiState.Success)
        assertEquals(SignInResultUiState.Success, handleSignIn.signInState().value)
        handleSignIn.saveSignInState(SignInResultUiState.Error(message = "test message"))
        assertEquals(
            SignInResultUiState.Error(message = "test message"),
            handleSignIn.signInState().value
        )
    }

    @Test
    fun test_credential_state() {
        val manageResource = TestManageResource()

        assertEquals(CredentialUiState.Initial, handleSignIn.credentialState().value)
        handleSignIn.saveCredentialState(CredentialUiState.Success(idToken = "123456789"))

        assertEquals(
            CredentialUiState.Success(idToken = "123456789"),
            handleSignIn.credentialState().value
        )

        handleSignIn.saveCredentialState(
            CredentialUiState.Error(manageResource = manageResource)
        )

        assertEquals(
            CredentialUiState.Error(manageResource = manageResource),
            handleSignIn.credentialState().value
        )
    }
}