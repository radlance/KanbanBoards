package com.github.radlance.ticket.edit.domain

import com.github.radlance.board.core.domain.EditTicket
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.core.domain.TicketRepository

interface EditTicketRepository : TicketRepository,
    com.github.radlance.ticket.info.domain.TicketInfoRepository {

    fun editTicket(ticket: EditTicket): UnitResult

    fun deleteTicket(ticketId: String): UnitResult
}