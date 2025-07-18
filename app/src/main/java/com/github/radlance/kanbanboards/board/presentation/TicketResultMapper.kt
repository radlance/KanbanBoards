package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.board.domain.TicketResult
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

class TicketResultMapper @Inject constructor(
    private val mapper: Column.Mapper<ColumnUi>
) : TicketResult.Mapper<TicketUiState> {

    override fun mapSuccess(tickets: List<Ticket>): TicketUiState = TicketUiState.Success(
        tickets.map { ticket ->
            with(ticket) {
                TicketUi(
                    colorHex = colorHex,
                    id = id,
                    name = name,
                    assignedMemberName = assignedMemberName,
                    column = column.map(mapper),
                    creationDate = creationDate.toKotlinLocalDateTime()
                )
            }
        }
    )

    override fun mapError(message: String): TicketUiState = TicketUiState.Error(message)
}