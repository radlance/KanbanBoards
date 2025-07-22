package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.presentation.BaseTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketMapperFacade
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

interface EditTicketMapperFacade : TicketMapperFacade {

    fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateEdit

    fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoEditUiState

    class Base @Inject constructor(
        private val boardMembersMapper: BoardMembersResult.Mapper<BoardMembersUiStateEdit>,
        private val ticketInfoUiMapper: TicketInfoResult.Mapper<TicketInfoEditUiState>,
        createTicketMapper: UnitResult.Mapper<TicketUiState>
    ) : BaseTicketMapperFacade(createTicketMapper), EditTicketMapperFacade {

        override fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateEdit {
            return boardMembersResult.map(boardMembersMapper)
        }

        override fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoEditUiState {
            return ticketInfoResult.map(ticketInfoUiMapper)
        }
    }
}