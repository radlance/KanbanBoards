package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.common.domain.BoardMembersResult
import com.github.radlance.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.presentation.BaseTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketMapperFacade
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import javax.inject.Inject

interface CreateTicketMapperFacade : TicketMapperFacade {

    fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateCreate

    class Base @Inject constructor(
        private val boardMembersMapper: BoardMembersResult.Mapper<BoardMembersUiStateCreate>,
        createTicketMapper: UnitResult.Mapper<TicketUiState>
    ) : BaseTicketMapperFacade(createTicketMapper), CreateTicketMapperFacade {
        override fun mapBoardMembersResult(
            boardMembersResult: BoardMembersResult
        ): BoardMembersUiStateCreate = boardMembersResult.map(boardMembersMapper)
    }
}