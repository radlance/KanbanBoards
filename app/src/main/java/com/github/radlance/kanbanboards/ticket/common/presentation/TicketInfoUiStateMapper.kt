package com.github.radlance.kanbanboards.ticket.common.presentation

import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.ticket.edit.presentation.TicketInfoEditUiState
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoUiState
import javax.inject.Inject

class TicketInfoUiStateMapper @Inject constructor(): TicketInfoUiState.Mapper<TicketInfoEditUiState> {

    override fun mapSuccess(ticket: Ticket): TicketInfoEditUiState = TicketInfoEditUiState.Loading

    override fun mapError(message: String): TicketInfoEditUiState = TicketInfoEditUiState.Error(message)

    override fun mapLoading(): TicketInfoEditUiState = TicketInfoEditUiState.Loading
}