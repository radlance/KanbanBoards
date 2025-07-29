package com.github.radlance.kanbanboards.common.presentation

interface UnitUiState {

    val buttonEnabled: Boolean

    val hasSize: Boolean
}

abstract class AbstractUnitUiState(
    override val hasSize: Boolean,
    override val buttonEnabled: Boolean = true
) : UnitUiState