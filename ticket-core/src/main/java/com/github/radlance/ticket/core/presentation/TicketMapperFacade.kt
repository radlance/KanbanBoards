package com.github.radlance.ticket.core.presentation

import com.github.radlance.core.domain.UnitResult

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