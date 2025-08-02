package com.github.radlance.board.core.presentation

import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.core.domain.BoardInfo
import javax.inject.Inject

class BoardResultMapper @Inject constructor() : BoardResult.Mapper<BoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo): BoardUiState = BoardUiState.Success(boardInfo)

    override fun mapError(message: String): BoardUiState = BoardUiState.Error(message)

    override fun mapNotExists(): BoardUiState = BoardUiState.NotExists
}