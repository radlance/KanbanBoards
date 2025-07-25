package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import javax.inject.Inject

class UpdateBoardNameMapper @Inject constructor(): UpdateBoardNameResult.Mapper<UpdateBoardNameUiState> {

    override fun mapSuccess() = UpdateBoardNameUiState.Success

    override fun mapError(message: String) = UpdateBoardNameUiState.Error(message)

    override fun mapAlreadyExists(message: String) = UpdateBoardNameUiState.AlreadyExists(message)
}