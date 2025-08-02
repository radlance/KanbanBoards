package com.github.radlance.board.core.domain

import kotlinx.coroutines.flow.Flow

interface BoardRepository {

    fun board(boardId: String): Flow<BoardResult>

    fun tickets(boardId: String): Flow<TicketResult>

    fun moveTicket(ticketId: String, column: Column)

    suspend fun leaveBoard(boardId: String)

    suspend fun deleteBoard(boardId: String)
}