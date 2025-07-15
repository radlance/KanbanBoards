package com.github.radlance.kanbanboards.ticket.create.domain

import kotlinx.coroutines.flow.Flow

interface TicketRepository {

    fun boardMembers(boardId: String): Flow<BoardMembersResult>
}