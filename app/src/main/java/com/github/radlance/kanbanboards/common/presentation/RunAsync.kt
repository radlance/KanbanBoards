package com.github.radlance.kanbanboards.common.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RunAsync {

    fun <T : Any> async(background: suspend () -> T, ui: (T) -> Unit, scope: CoroutineScope)

    fun <T> stateInViewModel(
        flow: Flow<T>,
        started: SharingStarted,
        initialValue: T,
        scope: CoroutineScope
    ): StateFlow<T>

    class Base @Inject constructor(
        private val dispatchersList: DispatchersList
    ) : RunAsync {
        override fun <T : Any> async(
            background: suspend () -> T,
            ui: (T) -> Unit,
            scope: CoroutineScope
        ) {
            scope.launch(dispatchersList.io()) {
                val result = background.invoke()

                withContext(dispatchersList.main()) {
                    ui.invoke(result)
                }
            }
        }

        override fun <T> stateInViewModel(
            flow: Flow<T>,
            started: SharingStarted,
            initialValue: T,
            scope: CoroutineScope
        ): StateFlow<T> = flow.stateIn(scope, started, initialValue)
    }
}