package com.github.radlance.kanbanboards.profile.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleProfile {

    val profileUiState: StateFlow<ProfileUiState>

    fun saveProfileUiState(profileUiState: ProfileUiState)

    class Base @Inject constructor() : HandleProfile {
        private val profileUiStateMutable = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)

        override val profileUiState = profileUiStateMutable.asStateFlow()

        override fun saveProfileUiState(profileUiState: ProfileUiState) {
            profileUiStateMutable.value = profileUiState
        }
    }
}