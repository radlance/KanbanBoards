package com.github.radlance.kanbanboars.profile.edit.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleEditProfile {

    val profileProviderUi: StateFlow<ProfileProviderUi>

    fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi)

    val profileCredentialUiState: StateFlow<ProfileCredentialUiState>

    fun saveProfileCredentialUiState(profileCredentialUiState: ProfileCredentialUiState)

    val deleteProfileUiState: StateFlow<DeleteProfileUiState>

    fun saveDeleteProfileUiState(deleteProfileUiState: DeleteProfileUiState)

    val editProfileUiState: StateFlow<EditProfileUiState>

    fun saveEditProfileUiState(editProfileUiState: EditProfileUiState)
}

internal class BaseHandleEditProfile @Inject constructor() : HandleEditProfile {

    private val profileProviderUiMutable = MutableStateFlow<ProfileProviderUi>(
        ProfileProviderUi.Initial
    )

    private val editProfileUiStateMutable = MutableStateFlow<EditProfileUiState>(
        EditProfileUiState.Initial
    )

    private val profileCredentialUiStateMutable = MutableStateFlow<ProfileCredentialUiState>(
        ProfileCredentialUiState.Initial
    )

    private val deleteProfileUiStateMutable = MutableStateFlow<DeleteProfileUiState>(
        DeleteProfileUiState.Initial
    )

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

    override val editProfileUiState: StateFlow<EditProfileUiState>
        get() = editProfileUiStateMutable.asStateFlow()

    override fun saveEditProfileUiState(editProfileUiState: EditProfileUiState) {
        editProfileUiStateMutable.value = editProfileUiState
    }
}