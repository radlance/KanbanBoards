package com.github.radlance.ticket.edit.presentation

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.common.presentation.BaseTicketMapperFacade
import com.github.radlance.ticket.common.presentation.TicketMapperFacade
import com.github.radlance.ticket.common.presentation.TicketUiState
import com.github.radlance.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

interface EditTicketMapperFacade : TicketMapperFacade {

    fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateEdit

    fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoEditUiState

    fun mapDeleteTicket(deleteTicketResult: UnitResult): DeleteTicketUiState

    class Base @Inject constructor(
        private val boardMembersMapper: BoardMembersResult.Mapper<BoardMembersUiStateEdit>,
        private val ticketInfoUiMapper: TicketInfoResult.Mapper<TicketInfoEditUiState>,
        private val deleteTicketMapper: UnitResult.Mapper<DeleteTicketUiState>,
        ticketMapper: UnitResult.Mapper<TicketUiState>
    ) : BaseTicketMapperFacade(ticketMapper), EditTicketMapperFacade {

        override fun mapBoardMembersResult(boardMembersResult: BoardMembersResult): BoardMembersUiStateEdit {
            return boardMembersResult.map(boardMembersMapper)
        }

        override fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoEditUiState {
            return ticketInfoResult.map(ticketInfoUiMapper)
        }

        override fun mapDeleteTicket(deleteTicketResult: UnitResult): DeleteTicketUiState {
            return deleteTicketResult.map(deleteTicketMapper)
        }
    }
}