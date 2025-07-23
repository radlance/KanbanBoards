package com.github.radlance.kanbanboards.board.create.domain

import kotlinx.coroutines.flow.Flow

interface CreateBoardRepository {

    suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult

    fun users(): Flow<SearchUsersResult>
}