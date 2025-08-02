package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.core.domain.UnitResult
import com.github.radlance.core.presentation.BaseViewModel
import com.github.radlance.core.presentation.RunAsync

abstract class BaseProfileViewModel(
    protected val handleProfile: HandleProfile,
    protected val facade: ProfileMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    protected fun handleDeleteProfile(action: suspend () -> UnitResult) {
        handleProfile.saveDeleteProfileUiState(DeleteProfileUiState.Loading)

        handle(background = action) {
            handleProfile.saveDeleteProfileUiState(facade.mapDeleteProfileResult(it))
        }
    }
}