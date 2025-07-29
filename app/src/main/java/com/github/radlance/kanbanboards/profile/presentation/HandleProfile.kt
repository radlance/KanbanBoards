package com.github.radlance.kanbanboards.profile.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleProfile {

    val profileUiState: StateFlow<ProfileUiState>

    fun saveProfileUiState(profileUiState: ProfileUiState)

    val profileProviderUi: StateFlow<ProfileProviderUi>

    fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi)

    class Base @Inject constructor() : HandleProfile {

        private val profileUiStateMutable = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)

        private val profileProviderUiMutable = MutableStateFlow<ProfileProviderUi>(
            ProfileProviderUi.Initial
        )

        override val profileUiState = profileUiStateMutable.asStateFlow()

        override fun saveProfileUiState(profileUiState: ProfileUiState) {
            profileUiStateMutable.value = profileUiState
        }

        override val profileProviderUi: StateFlow<ProfileProviderUi>
            get() = profileProviderUiMutable.asStateFlow()

        override fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi) {
            profileProviderUiMutable.value = profileProviderUi
        }
    }
}