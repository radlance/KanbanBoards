package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.data.BaseTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import javax.inject.Inject

class RemoteCreateTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val manageResource: ManageResource,
    boardRemoteDataSource: BoardRemoteDataSource
) : BaseTicketRepository(boardRemoteDataSource), CreateTicketRepository {

    override suspend fun createTicket(newTicket: NewTicket): UnitResult {
        return try {
            ticketRemoteDataSource.createTicket(newTicket)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(e.message ?: manageResource.string(R.string.error))
        }
    }
}