package com.github.radlance.kanbanboards.board.create.domain

import com.github.radlance.kanbanboards.common.domain.SearchUsersResult
import kotlinx.coroutines.flow.Flow

interface CreateBoardRepository {

    suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult

    fun users(): Flow<SearchUsersResult>
}