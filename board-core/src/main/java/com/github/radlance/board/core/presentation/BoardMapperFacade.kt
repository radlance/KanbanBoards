package com.github.radlance.board.core.presentation

import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.board.core.domain.Column
import com.github.radlance.board.core.domain.TicketResult
import javax.inject.Inject

interface BoardMapperFacade {

    fun mapBoardResult(result: BoardResult): BoardUiState

    fun mapTicketResult(result: TicketResult): TicketBoardUiState

    fun mapColumnUi(column: ColumnUi): Column
}

internal class BaseBoardMapperFacade @Inject constructor(
    private val boardResultMapper: BoardResult.Mapper<BoardUiState>,
    private val ticketResultMapper: TicketResult.Mapper<TicketBoardUiState>,
    private val columnUiMapper: ColumnUi.Mapper<Column>
) : BoardMapperFacade {
    override fun mapBoardResult(result: BoardResult): BoardUiState {
        return result.map(boardResultMapper)
    }

    override fun mapTicketResult(result: TicketResult): TicketBoardUiState {
        return result.map(ticketResultMapper)
    }

    override fun mapColumnUi(column: ColumnUi): Column {
        return column.map(columnUiMapper)
    }
}