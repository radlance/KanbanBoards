package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import javax.inject.Inject

class CreateTicketMapper @Inject constructor() : UnitResult.Mapper<TicketUiState> {

    override fun mapSuccess(): TicketUiState = TicketUiState.Success

    override fun mapError(message: String): TicketUiState = TicketUiState.Error(message)
}