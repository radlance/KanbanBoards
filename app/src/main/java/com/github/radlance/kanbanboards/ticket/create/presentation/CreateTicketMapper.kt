package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import javax.inject.Inject

class CreateTicketMapper @Inject constructor() : UnitResult.Mapper<CreateTicketUiState> {

    override fun mapSuccess(): CreateTicketUiState = CreateTicketUiState.Success

    override fun mapError(message: String): CreateTicketUiState = CreateTicketUiState.Error(message)
}