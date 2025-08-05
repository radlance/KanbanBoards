package com.github.radlance.kanbanboards.board.core.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

internal class TicketResultMapper @Inject constructor(
    private val mapper: Column.Mapper<ColumnUi>
) : TicketResult.Mapper<TicketBoardUiState> {

    override fun mapSuccess(tickets: List<Ticket>): TicketBoardUiState = TicketBoardUiState.Success(
        tickets.map { ticket ->
            with(ticket) {
                TicketUi(
                    colorHex = colorHex,
                    id = id,
                    name = name,
                    description = description,
                    assignedMemberNames = assignedMemberNames,
                    assignedMemberIds = assignedMemberIds,
                    column = column.map(mapper),
                    creationDate = creationDate.toKotlinLocalDateTime()
                )
            }
        }
    )

    override fun mapError(message: String): TicketBoardUiState = TicketBoardUiState.Error(message)
}