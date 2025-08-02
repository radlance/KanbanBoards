package com.github.radlance.boards.domain

import kotlinx.coroutines.flow.Flow

interface BoardsRepository {

    fun boards(): Flow<BoardsResult>
}