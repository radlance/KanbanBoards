package com.github.radlance.kanbanboards.createboard.domain

import kotlinx.coroutines.flow.Flow

interface CreateBoardRepository {

    suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult

    fun users(): Flow<SearchUsersResult>
}