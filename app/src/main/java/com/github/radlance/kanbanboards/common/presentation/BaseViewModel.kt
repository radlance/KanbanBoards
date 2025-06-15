package com.github.radlance.kanbanboards.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel(
    private val runAsync: RunAsync
) : ViewModel() {

    fun <T : Any> handle(background: suspend () -> T, ui: (T) -> Unit) {
        runAsync.runAsync(background, ui, viewModelScope)
    }

    fun <T> Flow<T>.stateInViewModel(initialValue: T): StateFlow<T> {
        return stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue
        )
    }
}