package com.github.radlance.ticket.create.domain

import com.github.radlance.board.core.domain.NewTicket
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.core.domain.TicketRepository

interface CreateTicketRepository : TicketRepository {

    fun createTicket(newTicket: NewTicket): UnitResult
}