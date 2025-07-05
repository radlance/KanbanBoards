package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardResult
import javax.inject.Inject

class BoardResultMapper @Inject constructor() : BoardResult.Mapper<BoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo): BoardUiState = BoardUiState.Success(boardInfo)

    override fun mapError(message: String): BoardUiState = BoardUiState.Error(message)
}