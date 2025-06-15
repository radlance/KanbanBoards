package com.github.radlance.kanbanboards.common.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RunAsync {

    fun <T : Any> runAsync(background: suspend () -> T, ui: (T) -> Unit, scope: CoroutineScope)

    class Base @Inject constructor(
        private val dispatchersList: DispatchersList
    ) : RunAsync {
        override fun <T : Any> runAsync(
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
    }
}