package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.core.presentation.BaseTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketMapperFacade
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketUiState
import javax.inject.Inject

interface CreateTicketMapperFacade : TicketMapperFacade {

    fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateCreate
}

internal class BaseCreateTicketMapperFacade @Inject constructor(
    private val boardMembersMapper: BoardMembersResult.Mapper<BoardMembersUiStateCreate>,
    createTicketMapper: UnitResult.Mapper<TicketUiState>
) : BaseTicketMapperFacade(createTicketMapper), CreateTicketMapperFacade {
    override fun mapBoardMembersResult(
        boardMembersResult: BoardMembersResult
    ): BoardMembersUiStateCreate = boardMembersResult.map(boardMembersMapper)
}