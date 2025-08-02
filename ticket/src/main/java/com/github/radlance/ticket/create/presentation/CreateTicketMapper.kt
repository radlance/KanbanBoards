package com.github.radlance.ticket.create.presentation

import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.common.presentation.TicketUiState
import javax.inject.Inject

class CreateTicketMapper @Inject constructor() : UnitResult.Mapper<TicketUiState> {

    override fun mapSuccess(): TicketUiState = TicketUiState.Success

    override fun mapError(message: String): TicketUiState = TicketUiState.Error(message)
}