package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.core.presentation.BaseViewModel
import com.github.radlance.kanbanboards.core.presentation.RunAsync

abstract class BaseEditProfileViewModel(
    protected val handleEditProfile: HandleEditProfile,
    protected val facade: EditProfileMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    protected fun handleEditProfile(action: suspend () -> UnitResult) {
        handleEditProfile.saveEditProfileUiState(EditProfileUiState.Loading)
        handle(background = action) {
            handleEditProfile.saveEditProfileUiState(facade.mapEditProfileResult(it))
        }
    }

    protected fun handleDeleteProfile(action: suspend () -> UnitResult) {
        handleEditProfile.saveDeleteProfileUiState(DeleteProfileUiState.Loading)

        handle(background = action) {
            handleEditProfile.saveDeleteProfileUiState(facade.mapDeleteProfileResult(it))
        }
    }
}