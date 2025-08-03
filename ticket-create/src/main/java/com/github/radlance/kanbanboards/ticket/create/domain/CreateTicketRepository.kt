package com.github.radlance.kanbanboards.ticket.create.domain

import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.core.domain.TicketRepository

interface CreateTicketRepository : TicketRepository {

    fun createTicket(newTicket: NewTicket): UnitResult
}