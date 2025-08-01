package com.github.radlance.common.data

import javax.inject.Inject

interface IgnoreHandle {

    fun handle(action: () -> Unit)

    suspend fun handleSuspend(action: suspend () -> Unit)
}

internal class BaseIgnoreHandle @Inject constructor() : IgnoreHandle {

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