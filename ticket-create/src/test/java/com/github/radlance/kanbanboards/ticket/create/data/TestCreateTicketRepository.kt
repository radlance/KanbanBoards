package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestCreateTicketRepository : CreateTicketRepository {
    val boardMembersCalledList = mutableListOf<Pair<String, String>>()
    private val boardMembersResult = MutableStateFlow<BoardMembersResult>(
        BoardMembersResult.Success(members = emptyList())
    )

    fun makeExpectedBoardMembersResult(boardMembersResult: BoardMembersResult) {
        this.boardMembersResult.value = boardMembersResult
    }

    val createTicketCalledList = mutableListOf<NewTicket>()
    private var createTicketResult: UnitResult = UnitResult.Success
    fun makeExpectedCreateTicketResult(createTicketResult: UnitResult) {
        this.createTicketResult = createTicketResult
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        boardMembersCalledList.add(Pair(boardId, ownerId))
        return boardMembersResult
    }

    override fun createTicket(newTicket: NewTicket): UnitResult {
        createTicketCalledList.add(newTicket)
        return createTicketResult
    }
}
