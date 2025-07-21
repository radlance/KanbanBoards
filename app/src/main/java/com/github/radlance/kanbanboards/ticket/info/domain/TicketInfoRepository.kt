package com.github.radlance.kanbanboards.ticket.info.domain

import kotlinx.coroutines.flow.Flow

interface TicketInfoRepository {

    fun ticket(ticketId: String): Flow<TicketInfoResult>
}