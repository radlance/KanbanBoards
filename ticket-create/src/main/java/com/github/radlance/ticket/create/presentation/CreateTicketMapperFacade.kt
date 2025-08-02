package com.github.radlance.ticket.create.presentation

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.core.presentation.BaseTicketMapperFacade
import com.github.radlance.ticket.core.presentation.TicketMapperFacade
import com.github.radlance.ticket.core.presentation.TicketUiState
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