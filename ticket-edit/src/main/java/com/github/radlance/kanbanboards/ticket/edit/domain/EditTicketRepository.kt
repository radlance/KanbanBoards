package com.github.radlance.kanbanboards.ticket.edit.domain

import com.github.radlance.kanbanboards.board.core.domain.EditTicket
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.core.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository

interface EditTicketRepository : TicketRepository, TicketInfoRepository {

    fun editTicket(ticket: EditTicket): UnitResult

    fun deleteTicket(ticketId: String): UnitResult
}