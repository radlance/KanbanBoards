package com.github.radlance.ticket.common.domain

import com.github.radlance.core.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>
}