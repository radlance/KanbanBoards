package com.github.radlance.ticket.create.data

import com.github.radlance.board.core.data.TicketRemoteDataSource
import com.github.radlance.board.core.domain.NewTicket
import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.core.domain.UsersRepository
import com.github.radlance.ticket.create.domain.CreateTicketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RemoteCreateTicketRepository @Inject constructor(
    private val ticketRemoteDataSource: TicketRemoteDataSource,
    private val manageResource: ManageResource,
    private val usersRepository: UsersRepository
) : CreateTicketRepository {

    override fun createTicket(newTicket: NewTicket): UnitResult {
        return try {
            ticketRemoteDataSource.createTicket(newTicket)
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(
                e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
            )
        }
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return usersRepository.boardMembers(boardId, ownerId)
    }
}