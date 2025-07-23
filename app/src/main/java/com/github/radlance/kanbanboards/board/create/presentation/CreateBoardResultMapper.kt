package com.github.radlance.kanbanboards.board.create.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.create.domain.CreateBoardResult
import javax.inject.Inject

class CreateBoardResultMapper @Inject constructor() : CreateBoardResult.Mapper<CreateBoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo): CreateBoardUiState =
        CreateBoardUiState.Success(boardInfo)

    override fun mapError(message: String): CreateBoardUiState = CreateBoardUiState.Error(message)

    override fun mapAlreadyExists(message: String): CreateBoardUiState =
        CreateBoardUiState.AlreadyExists(message)
}