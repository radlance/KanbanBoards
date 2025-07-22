package com.github.radlance.kanbanboards.ticket.edit.domain

import com.github.radlance.kanbanboards.ticket.common.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository

interface EditTicketRepository : TicketRepository, TicketInfoRepository