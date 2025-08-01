package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.common.presentation.RunAsync
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import com.github.radlance.login.presentation.signin.CredentialResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    facade: ProfileMapperFacade,
    handleProfile: HandleProfile,
    runAsync: RunAsync
) : BaseProfileViewModel(handleProfile, facade, runAsync), ProfileProviderAction {

    val profileUiState = handleProfile.profileUiState.onStart {
        handleProfile.saveProfileUiState(ProfileUiState.Loading)

        handle(background = profileRepository::profile) {
            handleProfile.saveProfileUiState(facade.mapLoadProfileResult(it))
        }
    }.stateInViewModel(initialValue = ProfileUiState.Initial)

    val profileProviderUi = handleProfile.profileProviderUi.onStart {

        handle(background = profileRepository::profileProvider) {
            handleProfile.saveProfileProviderUi(facade.mapProfileProvider(it))
        }
    }.stateInViewModel(initialValue = ProfileProviderUi.Initial)

    override val profileCredentialUiState = handleProfile.profileCredentialUiState

    val deleteProfileUiState = handleProfile.deleteProfileUiState

    fun signOut() {
        handle(background = profileRepository::signOut, ui = {})
    }

    override fun createCredential(credentialResult: CredentialResult) {
        handleProfile.saveProfileCredentialUiState(
            facade.mapProfileCredentialResult(credentialResult)
        )
    }

    override fun deleteProfile(userTokenId: String) = handleDeleteProfile {
        handleProfile.saveProfileCredentialUiState(ProfileCredentialUiState.Initial)
        profileRepository.deleteProfileWithGoogle(userTokenId)
    }

    override fun deleteProfile(email: String, password: String) = handleDeleteProfile {
        profileRepository.deleteProfileWithEmail(email, password)
    }
}

interface ProfileProviderAction : ProfileCredentialAction, ProfileEmailAction {

    val profileCredentialUiState: StateFlow<ProfileCredentialUiState>

    fun createCredential(credentialResult: CredentialResult)
}

interface ProfileCredentialAction {

    fun deleteProfile(userTokenId: String)
}

interface ProfileEmailAction {

    fun deleteProfile(email: String, password: String)
}