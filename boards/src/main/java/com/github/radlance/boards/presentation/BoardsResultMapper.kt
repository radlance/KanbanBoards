package com.github.radlance.boards.presentation

import com.github.radlance.boards.domain.BoardsResult
import com.github.radlance.core.domain.Board
import javax.inject.Inject

class BoardsResultMapper @Inject constructor(
    private val boardsMapper: Board.Mapper<BoardUi>
) : BoardsResult.Mapper<BoardsUiState> {

    override fun mapSuccess(boards: List<Board>): BoardsUiState {
        return BoardsUiState.Success(boards.map { it.map(boardsMapper) })
    }

    override fun mapError(message: String): BoardsUiState = BoardsUiState.Error(message)
}