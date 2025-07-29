package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val profileMapperFacade: ProfileMapperFacade,
    private val handleProfile: HandleProfile,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val profileUiState = handleProfile.profileUiState.onStart {
        handleProfile.saveProfileUiState(ProfileUiState.Loading)

        handle(background = profileRepository::profile) {
            handleProfile.saveProfileUiState(profileMapperFacade.mapLoadProfileResult(it))
        }
    }.stateInViewModel(initialValue = ProfileUiState.Initial)

    val profileProviderUi = handleProfile.profileProviderUi.onStart {

        handle(background = profileRepository::profileProvider) {
            handleProfile.saveProfileProviderUi(profileMapperFacade.mapProfileProvider(it))
        }
    }.stateInViewModel(initialValue = ProfileProviderUi.Initial)

    fun signOut() {
        handle(background = profileRepository::signOut, ui = {})
    }
}