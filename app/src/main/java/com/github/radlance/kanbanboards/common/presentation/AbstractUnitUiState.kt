package com.github.radlance.kanbanboards.common.presentation

interface UnitUiState {

    fun buttonEnabled(): Boolean

    fun hasSize(): Boolean
}

abstract class AbstractUnitUiState(
    private val hasSize: Boolean,
    private val buttonEnabled: Boolean
) : UnitUiState {

    override fun buttonEnabled(): Boolean = buttonEnabled

    override fun hasSize(): Boolean = hasSize
}