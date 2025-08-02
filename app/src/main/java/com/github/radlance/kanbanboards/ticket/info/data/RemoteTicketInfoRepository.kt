package com.github.radlance.kanbanboards.ticket.info.data

import com.github.radlance.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteTicketInfoRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource
) : TicketInfoRepository {

    override fun ticket(ticketId: String): Flow<TicketInfoResult> {
        return ticketRemoteDataSource.ticket(ticketId).map { ticket ->
            ticket?.let { TicketInfoResult.Success(it) } ?: TicketInfoResult.NotExists
        }.catch { e -> TicketInfoResult.Error(e.message!!) }
    }
}