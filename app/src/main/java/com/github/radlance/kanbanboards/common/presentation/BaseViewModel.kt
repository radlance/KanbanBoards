package com.github.radlance.kanbanboards.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel(
    private val runAsync: RunAsync
) : ViewModel() {

    protected fun <T : Any> handle(background: suspend () -> T, ui: (T) -> Unit) {
        runAsync.async(background, ui, viewModelScope)
    }

    protected fun <T> Flow<T>.stateInViewModel(initialValue: T): StateFlow<T> {
        return runAsync.stateInViewModel(
            flow = this,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = initialValue,
            scope = viewModelScope
        )
    }
}