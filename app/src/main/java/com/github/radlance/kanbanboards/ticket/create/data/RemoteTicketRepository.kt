package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val manageResource: ManageResource
) : TicketRepository {

    override fun boardMembers(boardId: String): Flow<BoardMembersResult> {
        return ticketRemoteDataSource.boardMembers(boardId).map {
            BoardMembersResult.Success(it)
        }.catch { e -> BoardMembersResult.Error(e.message!!) }
    }

    override suspend fun createTicket(newTicket: NewTicket): UnitResult {
        return try {
            ticketRemoteDataSource.createTicket(newTicket)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(e.message ?: manageResource.string(R.string.error))
        }
    }
}