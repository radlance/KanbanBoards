package com.github.radlance.kanbanboards.board.core.data

import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteBoardRepository @Inject constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource,
    private val ticketRemoteDataSource: TicketRemoteDataSource
) : BoardRepository {

    override fun board(boardId: String): Flow<BoardResult> {
        return boardRemoteDataSource.board(boardId).map { boardInfo ->
            boardInfo?.let { BoardResult.Success(it) } ?: BoardResult.NotExists
        }.catch { e -> BoardResult.Error(e.message!!) }
    }

    override fun tickets(boardId: String): Flow<TicketResult> {
        return ticketRemoteDataSource.tickets(boardId).map {
            TicketResult.Success(it)
        }.catch { e -> TicketResult.Error(e.message!!) }
    }

    override fun moveTicket(ticketId: String, column: Column) {
        ticketRemoteDataSource.moveTicket(ticketId, column)
    }

    override suspend fun leaveBoard(boardId: String) {
        boardRemoteDataSource.leaveBoard(boardId)
    }

    override suspend fun deleteBoard(boardId: String) {
        try {
            boardRemoteDataSource.deleteBoard(boardId)
        } catch (_: Exception) {
        }
    }
}