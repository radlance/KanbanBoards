package com.github.radlance.kanbanboards.ticket.common.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.create.presentation.TicketUiState

interface TicketMapperFacade {


    fun mapCreateTicketResult(unitResult: UnitResult): TicketUiState
}

abstract class BaseTicketMapperFacade(
    private val unitResultMapper: UnitResult.Mapper<TicketUiState>
) : TicketMapperFacade {

    override fun mapCreateTicketResult(unitResult: UnitResult): TicketUiState {
        return unitResult.map(unitResultMapper)
    }
}