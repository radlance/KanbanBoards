package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.core.domain.UnitResult
import javax.inject.Inject

internal class DeleteTicketMapper @Inject constructor() : UnitResult.Mapper<DeleteTicketUiState> {

    override fun mapSuccess(): DeleteTicketUiState = DeleteTicketUiState.Success

    override fun mapError(message: String): DeleteTicketUiState = DeleteTicketUiState.Error(message)
}