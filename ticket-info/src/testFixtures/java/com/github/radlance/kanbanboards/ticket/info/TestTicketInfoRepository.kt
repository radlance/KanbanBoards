package com.github.radlance.kanbanboards.ticket.info

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

class TestTicketInfoRepository : TicketInfoRepository {

    private val ticketInfoResult = MutableStateFlow<TicketInfoResult>(
        TicketInfoResult.Success(
            Ticket(
                id = "initial id",
                colorHex = "initial color",
                name = "initial name",
                description = "initial description",
                assignedMemberName = "initial assignee member name",
                column = Column.Todo,
                creationDate = LocalDateTime.of(2025, 1, 1, 1, 1),
                assignedMemberId = "initial assigned member id"
            )
        )
    )

    val ticketCalledList = mutableListOf<String>()

    fun makeExpectedTicketInfoResult(ticketInfoResult: TicketInfoResult) {
        this.ticketInfoResult.value = ticketInfoResult
    }

    override fun ticket(ticketId: String): Flow<TicketInfoResult> {
        ticketCalledList.add(ticketId)
        return ticketInfoResult
    }
}