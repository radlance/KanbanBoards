package com.github.radlance.kanbanboards.ticket.common.data

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.ticket.common.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

abstract class BaseTicketRepository(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : TicketRepository {

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return boardRemoteDataSource.boardMembers(boardId, ownerId).map {
            BoardMembersResult.Success(it)
        }.catch { e -> BoardMembersResult.Error(e.message!!) }
    }
}