package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource
) : TicketRepository {

    override fun boardMembers(boardId: String): Flow<BoardMembersResult> {
        return ticketRemoteDataSource.boardMembers(boardId).map {
            BoardMembersResult.Success(it)
        }.catch { e -> BoardMembersResult.Error(e.message!!) }
    }
}