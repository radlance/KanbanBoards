package com.github.radlance.kanbanboards.boards.presentation

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import javax.inject.Inject

class BoardsResultMapper @Inject constructor(
    private val boardsMapper: Board.Mapper<BoardUi>
) : BoardsResult.Mapper<BoardsUiState> {

    override fun mapSuccess(boards: List<Board>): BoardsUiState {
        return BoardsUiState.Success(boards.map { it.map(boardsMapper) })
    }

    override fun mapError(message: String): BoardsUiState = BoardsUiState.Error(message)
}