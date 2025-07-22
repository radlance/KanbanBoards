package com.github.radlance.kanbanboards.ticket.common.domain

import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>
}