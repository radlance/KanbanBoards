package com.github.radlance.kanbanboards.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val dispatchersList: DispatchersList
) : ViewModel() {

    fun <T : Any> handle(background: suspend () -> T, ui: (T) -> Unit) {
        viewModelScope.launch(dispatchersList.io()) {
            val result = background.invoke()

            withContext(dispatchersList.main()) {
                ui.invoke(result)
            }
        }
    }

    fun <T> Flow<T>.stateInViewModel(initialValue: T): StateFlow<T> {
        return stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue
        )
    }
}