package com.github.radlance.kanbanboards.board.domain

import kotlinx.coroutines.flow.Flow

interface BoardRepository {

    fun board(boardId: String): Flow<BoardResult>

    fun tickets(boardId: String): Flow<TicketResult>
}