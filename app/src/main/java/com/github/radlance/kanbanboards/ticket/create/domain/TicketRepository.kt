package com.github.radlance.kanbanboards.ticket.create.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.board.domain.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String): Flow<BoardMembersResult>

    suspend fun createTicket(newTicket: NewTicket): UnitResult
}