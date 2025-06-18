package com.github.radlance.kanbanboards.boards.domain

import kotlinx.coroutines.flow.Flow

interface BoardsRepository {

    fun boards(): Flow<List<Board>>
}