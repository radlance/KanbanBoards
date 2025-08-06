package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.core.presentation.BaseViewModel
import com.github.radlance.kanbanboards.core.presentation.RunAsync
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    facade: ProfileMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val profileUiState = profileRepository.profile().map {
        facade.mapLoadProfileResult(it)
    }.stateInViewModel(ProfileUiState.Loading)

    fun signOut() {
        handle(background = profileRepository::signOut, ui = {})
    }
}