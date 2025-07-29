package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync

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