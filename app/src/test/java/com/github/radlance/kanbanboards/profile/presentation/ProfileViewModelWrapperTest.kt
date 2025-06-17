package com.github.radlance.kanbanboards.profile.presentation

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelWrapperTest {

    private lateinit var profileViewModelWrapper: ProfileViewModelWrapper

    @Before
    fun setup() {
        profileViewModelWrapper = ProfileViewModelWrapper.Base(
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun test_profile_ui_state() {
        assertEquals(ProfileUiState.Initial, profileViewModelWrapper.profileUiState().value)
        profileViewModelWrapper.saveProfileUiState(ProfileUiState.Loading)
        assertEquals(ProfileUiState.Loading, profileViewModelWrapper.profileUiState().value)

        profileViewModelWrapper.saveProfileUiState(
            ProfileUiState.Base(
                name = "name",
                email = "email@example.com"
            )
        )

        assertEquals(
            ProfileUiState.Base(
                name = "name",
                email = "email@example.com"
            ),
            profileViewModelWrapper.profileUiState().value
        )
    }
}