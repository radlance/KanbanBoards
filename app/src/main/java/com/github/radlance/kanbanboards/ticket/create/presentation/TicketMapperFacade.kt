package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import javax.inject.Inject

interface TicketMapperFacade {

    fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiState

    fun mapCreateTicketResult(unitResult: UnitResult): CreateTicketUiState

    class Base @Inject constructor(
        private val boardMembersMapper: BoardMembersResult.Mapper<BoardMembersUiState>,
        private val createTicketMapper: UnitResult.Mapper<CreateTicketUiState>
    ) : TicketMapperFacade {
        override fun mapBoardMembersResult(
            boardMembersResult: BoardMembersResult
        ): BoardMembersUiState = boardMembersResult.map(boardMembersMapper)

        override fun mapCreateTicketResult(
            unitResult: UnitResult
        ): CreateTicketUiState = unitResult.map(createTicketMapper)
    }
}