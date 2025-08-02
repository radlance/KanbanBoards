package com.github.radlance.ticket.edit.data

import com.github.radlance.board.core.data.TicketRemoteDataSource
import com.github.radlance.board.core.domain.EditTicket
import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.core.domain.UsersRepository
import com.github.radlance.ticket.edit.domain.EditTicketRepository
import com.github.radlance.ticket.info.domain.TicketInfoRepository
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