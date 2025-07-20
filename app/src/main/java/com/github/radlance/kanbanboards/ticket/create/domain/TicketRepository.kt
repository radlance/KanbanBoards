package com.github.radlance.kanbanboards.ticket.create.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult
import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>

    suspend fun createTicket(newTicket: NewTicket): UnitResult
}