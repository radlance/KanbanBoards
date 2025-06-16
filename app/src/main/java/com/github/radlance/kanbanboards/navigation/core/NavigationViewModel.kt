package com.github.radlance.kanbanboards.navigation.core

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    navigationRepository: NavigationRepository,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val authorized: StateFlow<AuthorizedUiState> = navigationRepository.authorizedStatus().map {
        if (it) AuthorizedUiState.Authorized else AuthorizedUiState.Unauthorized
    }.stateInViewModel(initialValue = AuthorizedUiState.Unauthorized)
}