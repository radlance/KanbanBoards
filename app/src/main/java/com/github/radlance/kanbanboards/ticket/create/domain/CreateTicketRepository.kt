package com.github.radlance.kanbanboards.ticket.create.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.domain.TicketRepository

interface CreateTicketRepository : TicketRepository {

    suspend fun createTicket(newTicket: NewTicket): UnitResult
}