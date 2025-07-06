package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.TicketResult
import javax.inject.Inject

interface BoardMapperFacade {

    fun mapBoardResult(result: BoardResult): BoardUiState

    fun mapTicketResult(result: TicketResult): TicketUiState

    fun mapColumnUi(column: ColumnUi): Column

    class Base @Inject constructor(
        private val boardResultMapper: BoardResult.Mapper<BoardUiState>,
        private val ticketResultMapper: TicketResult.Mapper<TicketUiState>,
        private val columnUiMapper: ColumnUi.Mapper<Column>
    ) : BoardMapperFacade {
        override fun mapBoardResult(result: BoardResult): BoardUiState {
            return result.map(boardResultMapper)
        }

        override fun mapTicketResult(result: TicketResult): TicketUiState {
            return result.map(ticketResultMapper)
        }

        override fun mapColumnUi(column: ColumnUi): Column {
            return column.map(columnUiMapper)
        }
    }
}