package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

class TicketInfoEditMapper @Inject constructor() : TicketInfoResult.Mapper<TicketInfoEditUiState> {

    override fun mapSuccess(ticket: Ticket): TicketInfoEditUiState =
        TicketInfoEditUiState.Success(ticket)


    override fun mapError(message: String): TicketInfoEditUiState =
        TicketInfoEditUiState.Error(message)
}