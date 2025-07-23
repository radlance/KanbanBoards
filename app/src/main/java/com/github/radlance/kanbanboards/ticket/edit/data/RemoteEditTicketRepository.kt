package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.data.BaseTicketRepository
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import javax.inject.Inject

class RemoteEditTicketRepository @Inject constructor(
    private val ticketInfoRepository: TicketInfoRepository,
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val handleUnitResult: HandleUnitResult,
    boardRemoteDataSource: BoardRemoteDataSource
) : BaseTicketRepository(boardRemoteDataSource), EditTicketRepository {

    override suspend fun editTicket(ticket: EditTicket): UnitResult = handleUnitResult.handle {
        ticketRemoteDataSource.editTicket(ticket)
    }

    override suspend fun deleteTicket(ticketId: String): UnitResult = handleUnitResult.handle {
        ticketRemoteDataSource.deleteTicket(ticketId)
    }

    override fun ticket(ticketId: String) = ticketInfoRepository.ticket(ticketId)
}