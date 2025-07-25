package com.github.radlance.kanbanboards.ticket.info.presentation

import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

class TicketInfoResultMapper @Inject constructor() : TicketInfoResult.Mapper<TicketInfoUiState> {

    override fun mapSuccess(ticket: Ticket): TicketInfoUiState = TicketInfoUiState.Success(ticket)

    override fun mapError(message: String): TicketInfoUiState = TicketInfoUiState.Error(message)

    override fun mapNotExists(): TicketInfoUiState = TicketInfoUiState.NotExists
}