package com.github.radlance.kanbanboards.common.domain

import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun users(): Flow<SearchUsersResult>

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>
}