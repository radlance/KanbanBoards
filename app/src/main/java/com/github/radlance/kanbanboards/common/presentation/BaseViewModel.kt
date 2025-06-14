package com.github.radlance.kanbanboards.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}