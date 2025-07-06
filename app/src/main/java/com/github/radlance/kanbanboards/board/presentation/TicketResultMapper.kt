package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.board.domain.TicketResult
import javax.inject.Inject

class TicketResultMapper @Inject constructor(
    private val mapper: Column.Mapper<ColumnUi>
) : TicketResult.Mapper<TicketUiState> {

    override fun mapSuccess(tickets: List<Ticket>): TicketUiState = TicketUiState.Success(
        tickets.map { ticket ->
            with(ticket) { TicketUi(colorHex, id, name, assignedMemberName, column.map(mapper)) }
        }
    )

    override fun mapError(message: String): TicketUiState = TicketUiState.Error(message)
}