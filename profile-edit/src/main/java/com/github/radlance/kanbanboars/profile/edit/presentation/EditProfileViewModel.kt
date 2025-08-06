package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.presentation.RunAsync
import com.github.radlance.kanbanboars.profile.edit.domain.EditProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: EditProfileRepository,
    facade: EditProfileMapperFacade,
    handleEditProfile: HandleEditProfile,
    runAsync: RunAsync
) : BaseEditProfileViewModel(handleEditProfile, facade, runAsync), EditProfileAction {

    val profileUiState = repository.profile().map {
        facade.mapLoadProfileResult(it)
    }.stateInViewModel(ProfileEditUiState.Loading)

    override val profileProviderUi = handleEditProfile.profileProviderUi.onStart {

        handle(background = repository::profileProvider) {
            handleEditProfile.saveProfileProviderUi(facade.mapProfileProvider(it))
        }
    }.stateInViewModel(initialValue = ProfileProviderUi.Initial)

    override val profileCredentialUiState = handleEditProfile.profileCredentialUiState

    val deleteProfileUiState = handleEditProfile.deleteProfileUiState

    override val editProfileUiState = handleEditProfile.editProfileUiState

    override fun createCredential(credentialResult: CredentialResult) {
        handleEditProfile.saveProfileCredentialUiState(
            facade.mapProfileCredentialResult(credentialResult)
        )
    }

    override fun editProfile(newName: String) = handleEditProfile {
        repository.editProfile(newName)
    }

    override fun deleteProfile(userTokenId: String) = handleDeleteProfile {
        handleEditProfile.saveProfileCredentialUiState(ProfileCredentialUiState.Initial)
        repository.deleteProfile(userTokenId)
    }

    override fun deleteProfile(email: String, password: String) = handleDeleteProfile {
        repository.deleteProfile(email, password)
    }

    override fun resetEditProfileUiState() {
        handleEditProfile.saveEditProfileUiState(EditProfileUiState.Initial)
    }
}

interface EditProfileAction : ProfileProviderAction {

    val profileProviderUi: StateFlow<ProfileProviderUi>

    val editProfileUiState: StateFlow<EditProfileUiState>

    fun editProfile(newName: String)

    fun resetEditProfileUiState()
}

interface ProfileProviderAction : ProfileEmailAction, ProfileCredentialAction {

    val profileCredentialUiState: StateFlow<ProfileCredentialUiState>

    fun createCredential(credentialResult: CredentialResult)
}

interface ProfileCredentialAction {

    fun deleteProfile(userTokenId: String)
}

interface ProfileEmailAction {

    fun deleteProfile(email: String, password: String)
}