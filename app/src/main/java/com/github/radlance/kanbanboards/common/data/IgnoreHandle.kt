package com.github.radlance.kanbanboards.common.data

import javax.inject.Inject

interface IgnoreHandle {

    fun handle(action: () -> Unit)

    suspend fun handleSuspend(action: suspend () -> Unit)

    class Base @Inject constructor() : IgnoreHandle {

        override fun handle(action: () -> Unit) {
            try {
                action.invoke()
            } catch (_: Exception) {
            }
        }

        override suspend fun handleSuspend(action: suspend () -> Unit) {
            try {
                action.invoke()
            } catch (_: Exception) {
            }
        }
    }
}