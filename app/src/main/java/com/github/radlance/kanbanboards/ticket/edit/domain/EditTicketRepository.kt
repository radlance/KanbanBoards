package com.github.radlance.kanbanboards.ticket.edit.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository

interface EditTicketRepository : TicketRepository, TicketInfoRepository {

    fun editTicket(ticket: EditTicket): UnitResult

    fun deleteTicket(ticketId: String): UnitResult
}