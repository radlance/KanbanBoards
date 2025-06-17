package com.github.radlance.kanbanboards.profile.presentation

import androidx.lifecycle.viewModelScope
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val profileMapper: LoadProfileResult.Mapper<ProfileUiState>,
    private val profileViewModelWrapper: ProfileViewModelWrapper,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val profileUiState = profileViewModelWrapper.profileUiState().onStart {
        profileViewModelWrapper.saveProfileUiState(ProfileUiState.Loading)

        handle(background = profileRepository::profile) {
            profileViewModelWrapper.saveProfileUiState(it.map(profileMapper))
        }
    }.stateInViewModel(initialValue = ProfileUiState.Initial)

    fun signOut() {
        handle(background = profileRepository::signOut, ui = {})
    }
}