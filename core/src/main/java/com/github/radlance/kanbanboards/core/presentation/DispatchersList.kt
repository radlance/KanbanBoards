package com.github.radlance.kanbanboards.core.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatchersList {

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher
}

internal class BaseDispatchersList @Inject constructor() : DispatchersList {

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun main(): CoroutineDispatcher = Dispatchers.Main
}