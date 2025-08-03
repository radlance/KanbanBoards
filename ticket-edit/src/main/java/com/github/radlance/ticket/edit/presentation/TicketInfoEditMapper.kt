package com.github.radlance.ticket.edit.presentation

import com.github.radlance.board.core.domain.Ticket
import com.github.radlance.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

internal class TicketInfoEditMapper @Inject constructor() :
    TicketInfoResult.Mapper<TicketInfoEditUiState> {

    override fun mapSuccess(ticket: Ticket) = TicketInfoEditUiState.Success(ticket)

    override fun mapError(message: String) = TicketInfoEditUiState.Error(message)

    override fun mapNotExists(): TicketInfoEditUiState = TicketInfoEditUiState.NotExists
}