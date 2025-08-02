package com.github.radlance.kanbanboards.ticket.info.presentation

import com.github.radlance.board.core.domain.Column
import com.github.radlance.board.core.presentation.ColumnUi
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import javax.inject.Inject

interface TicketInfoFacade {

    fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoUiState

    fun mapColumnUi(columnUi: ColumnUi): Column

    class Base @Inject constructor(
        private val ticketInfoMapper: TicketInfoResult.Mapper<TicketInfoUiState>,
        private val columnUiMapper: ColumnUi.Mapper<Column>
    ) : TicketInfoFacade {

        override fun mapTicketInfoResult(ticketInfoResult: TicketInfoResult): TicketInfoUiState {
            return ticketInfoResult.map(ticketInfoMapper)
        }

        override fun mapColumnUi(columnUi: ColumnUi): Column {
            return columnUi.map(columnUiMapper)
        }
    }
}