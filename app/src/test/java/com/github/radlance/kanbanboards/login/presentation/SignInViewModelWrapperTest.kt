package com.github.radlance.kanbanboards.login.presentation

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.common.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignInViewModelWrapperTest : BaseTest() {

    private lateinit var signInViewModelWrapper: SignInViewModelWrapper

    @Before
    fun setup() {
        signInViewModelWrapper = SignInViewModelWrapper.Base(SavedStateHandle())
    }

    @Test
    fun test_sign_in_state() {
        assertEquals(SignInResultUiState.Initial, signInViewModelWrapper.signInState().value)
        signInViewModelWrapper.saveSignInState(SignInResultUiState.Loading)
        assertEquals(SignInResultUiState.Loading, signInViewModelWrapper.signInState().value)
        signInViewModelWrapper.saveSignInState(SignInResultUiState.Success)
        assertEquals(SignInResultUiState.Success, signInViewModelWrapper.signInState().value)
        signInViewModelWrapper.saveSignInState(SignInResultUiState.Error(message = "test message"))
        assertEquals(
            SignInResultUiState.Error(message = "test message"),
            signInViewModelWrapper.signInState().value
        )
    }

    @Test
    fun test_credential_state() {
        val manageResource = TestManageResource()

        assertEquals(CredentialUiState.Initial, signInViewModelWrapper.credentialState().value)
        signInViewModelWrapper.saveCredentialState(CredentialUiState.Success(idToken = "123456789"))

        assertEquals(
            CredentialUiState.Success(idToken = "123456789"),
            signInViewModelWrapper.credentialState().value
        )

        signInViewModelWrapper.saveCredentialState(
            CredentialUiState.Error(manageResource = manageResource)
        )

        assertEquals(
            CredentialUiState.Error(manageResource = manageResource),
            signInViewModelWrapper.credentialState().value
        )
    }
}