package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.domain.BoardMembersResult
import com.github.radlance.common.domain.UnitResult
import com.github.radlance.common.domain.UsersRepository
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteCreateTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val manageResource: ManageResource,
    private val usersRepository: UsersRepository
) : CreateTicketRepository {

    override fun createTicket(newTicket: NewTicket): UnitResult {
        return try {
            ticketRemoteDataSource.createTicket(newTicket)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(e.message ?: manageResource.string(R.string.error))
        }
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return usersRepository.boardMembers(boardId, ownerId)
    }
}