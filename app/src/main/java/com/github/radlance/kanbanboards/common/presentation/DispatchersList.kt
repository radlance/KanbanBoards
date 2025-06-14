package com.github.radlance.kanbanboards.common.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatchersList {

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher

    class Base @Inject constructor(): DispatchersList {

        override fun io(): CoroutineDispatcher = Dispatchers.IO

        override fun main(): CoroutineDispatcher = Dispatchers.Main
    }
}