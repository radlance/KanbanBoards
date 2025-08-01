package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.UsersRepository
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteEditTicketRepository @Inject constructor(
    private val ticketInfoRepository: TicketInfoRepository,
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val handleUnitResult: HandleUnitResult,
    private val usersRepository: UsersRepository
) : EditTicketRepository {

    override fun editTicket(ticket: EditTicket): UnitResult = handleUnitResult.handle {
        ticketRemoteDataSource.editTicket(ticket)
    }

    override fun deleteTicket(ticketId: String): UnitResult = handleUnitResult.handle {
        ticketRemoteDataSource.deleteTicket(ticketId)
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return usersRepository.boardMembers(boardId, ownerId)
    }

    override fun ticket(ticketId: String) = ticketInfoRepository.ticket(ticketId)
}