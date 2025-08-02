package com.github.radlance.core.domain

import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun users(): Flow<SearchUsersResult>

    fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult>
}