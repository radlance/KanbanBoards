package com.github.radlance.ticket.create.presentation

import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.core.presentation.TicketUiState
import javax.inject.Inject

internal class CreateTicketMapper @Inject constructor() : UnitResult.Mapper<TicketUiState> {

    override fun mapSuccess(): TicketUiState = TicketUiState.Success

    override fun mapError(message: String): TicketUiState = TicketUiState.Error(message)
}