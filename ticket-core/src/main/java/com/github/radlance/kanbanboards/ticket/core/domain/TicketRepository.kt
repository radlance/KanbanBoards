package com.github.radlance.kanbanboards.ticket.core.domain

import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>
}