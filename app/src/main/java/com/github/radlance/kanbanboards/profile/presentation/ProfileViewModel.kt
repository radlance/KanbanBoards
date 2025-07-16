package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val profileMapper: LoadProfileResult.Mapper<ProfileUiState>,
    private val handleProfile: HandleProfile,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val profileUiState = handleProfile.profileUiState.onStart {
        handleProfile.saveProfileUiState(ProfileUiState.Loading)

        handle(background = profileRepository::profile) {
            handleProfile.saveProfileUiState(it.map(profileMapper))
        }
    }.stateInViewModel(initialValue = ProfileUiState.Initial)

    fun signOut() {
        handle(background = profileRepository::signOut, ui = {})
    }
}