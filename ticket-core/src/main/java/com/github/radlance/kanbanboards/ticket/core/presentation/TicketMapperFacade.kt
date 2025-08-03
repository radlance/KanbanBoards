package com.github.radlance.kanbanboards.ticket.core.presentation

import com.github.radlance.kanbanboards.core.domain.UnitResult

interface TicketMapperFacade {

    fun mapTicketResult(unitResult: UnitResult): TicketUiState
}

abstract class BaseTicketMapperFacade(
    private val unitResultMapper: UnitResult.Mapper<TicketUiState>
) : TicketMapperFacade {

    override fun mapTicketResult(unitResult: UnitResult): TicketUiState {
        return unitResult.map(unitResultMapper)
    }
}