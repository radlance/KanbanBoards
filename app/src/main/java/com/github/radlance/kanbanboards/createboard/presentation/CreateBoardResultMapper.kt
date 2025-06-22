package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import javax.inject.Inject

class CreateBoardResultMapper @Inject constructor() : CreateBoardResult.Mapper<CreateBoardUiState> {

    override fun mapSuccess(): CreateBoardUiState = CreateBoardUiState.Success

    override fun mapError(message: String): CreateBoardUiState = CreateBoardUiState.Error(message)

    override fun mapAlreadyExists(message: String): CreateBoardUiState =
        CreateBoardUiState.AlreadyExists(message)
}