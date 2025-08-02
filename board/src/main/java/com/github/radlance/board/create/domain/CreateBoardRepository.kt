package com.github.radlance.board.create.domain

import com.github.radlance.core.domain.SearchUsersResult
import kotlinx.coroutines.flow.Flow

interface CreateBoardRepository {

    suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult

    fun users(): Flow<SearchUsersResult>
}