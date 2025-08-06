package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.core.data.HandleUnitResult
import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.core.domain.UsersRepository
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RemoteCreateTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val handleUnitResult: HandleUnitResult,
    private val usersRepository: UsersRepository
) : CreateTicketRepository {

    override fun createTicket(newTicket: NewTicket): UnitResult = handleUnitResult.handle {
        ticketRemoteDataSource.createTicket(newTicket)
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return usersRepository.boardMembers(boardId, ownerId)
    }
}