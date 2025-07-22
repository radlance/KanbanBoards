package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.ticket.common.data.BaseTicketRepository
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import javax.inject.Inject

class RemoteEditTicketRepository @Inject constructor(
    private val ticketInfoRepository: TicketInfoRepository,
    boardRemoteDataSource: BoardRemoteDataSource
) : BaseTicketRepository(boardRemoteDataSource), EditTicketRepository {

    override fun ticket(ticketId: String) = ticketInfoRepository.ticket(ticketId)
}