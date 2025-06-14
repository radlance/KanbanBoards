package com.github.radlance.kanbanboards.navigation.core

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    navigationRepository: NavigationRepository,
    dispatchersList: DispatchersList
) : BaseViewModel(dispatchersList) {

    val authorized = navigationRepository.authorizedStatus().map {
        if (it) AuthorizedUiState.Authorized else AuthorizedUiState.UnAuthorized
    }.stateInViewModel(initialValue = AuthorizedUiState.UnAuthorized)
}