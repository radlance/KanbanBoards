package com.github.radlance.profile.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleProfile {

    val profileUiState: StateFlow<ProfileUiState>

    fun saveProfileUiState(profileUiState: ProfileUiState)

    val profileProviderUi: StateFlow<ProfileProviderUi>

    fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi)

    val profileCredentialUiState: StateFlow<ProfileCredentialUiState>

    fun saveProfileCredentialUiState(profileCredentialUiState: ProfileCredentialUiState)

    val deleteProfileUiState: StateFlow<DeleteProfileUiState>

    fun saveDeleteProfileUiState(deleteProfileUiState: DeleteProfileUiState)
}

internal class BaseHandleProfile @Inject constructor() : HandleProfile {

    private val profileUiStateMutable = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)

    private val profileProviderUiMutable = MutableStateFlow<ProfileProviderUi>(
        ProfileProviderUi.Initial
    )

    private val profileCredentialUiStateMutable = MutableStateFlow<ProfileCredentialUiState>(
        ProfileCredentialUiState.Initial
    )

    private val deleteProfileUiStateMutable = MutableStateFlow<DeleteProfileUiState>(
        DeleteProfileUiState.Initial
    )

    override val profileUiState get() = profileUiStateMutable.asStateFlow()

    override fun saveProfileUiState(profileUiState: ProfileUiState) {
        profileUiStateMutable.value = profileUiState
    }

    override val profileProviderUi: StateFlow<ProfileProviderUi>
        get() = profileProviderUiMutable.asStateFlow()

    override fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi) {
        profileProviderUiMutable.value = profileProviderUi
    }

    override val profileCredentialUiState: StateFlow<ProfileCredentialUiState>
        get() = profileCredentialUiStateMutable.asStateFlow()

    override fun saveProfileCredentialUiState(profileCredentialUiState: ProfileCredentialUiState) {
        profileCredentialUiStateMutable.value = profileCredentialUiState
    }

    override val deleteProfileUiState: StateFlow<DeleteProfileUiState>
        get() = deleteProfileUiStateMutable.asStateFlow()

    override fun saveDeleteProfileUiState(deleteProfileUiState: DeleteProfileUiState) {
        deleteProfileUiStateMutable.value = deleteProfileUiState
    }
}