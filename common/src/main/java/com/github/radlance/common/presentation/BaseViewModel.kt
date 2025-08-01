package com.github.radlance.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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
        return runAsync.stateInAsync(
            flow = this,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = initialValue,
            scope = viewModelScope
        )
    }

    protected fun <T> Flow<T>.launchInViewModel(): Job {
        return runAsync.launchInAsync(flow = this, coroutineScope = viewModelScope)
    }
}