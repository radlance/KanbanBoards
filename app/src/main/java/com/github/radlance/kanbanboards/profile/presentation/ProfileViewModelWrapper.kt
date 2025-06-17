package com.github.radlance.kanbanboards.profile.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ProfileViewModelWrapper {

    fun profileUiState(): StateFlow<ProfileUiState>

    fun saveProfileUiState(profileUiState: ProfileUiState)

    class Base @Inject constructor(
        private val savedStateHandle: SavedStateHandle
    ) : ProfileViewModelWrapper {
        override fun profileUiState(): StateFlow<ProfileUiState> {
            return savedStateHandle.getStateFlow(KEY_PROFILE, initialValue = ProfileUiState.Initial)
        }

        override fun saveProfileUiState(profileUiState: ProfileUiState) {
            savedStateHandle[KEY_PROFILE] = profileUiState
        }

        companion object {
            private const val KEY_PROFILE = "profile"
        }
    }
}