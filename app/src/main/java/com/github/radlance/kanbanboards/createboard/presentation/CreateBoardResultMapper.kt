package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import javax.inject.Inject

class CreateBoardResultMapper @Inject constructor(
    private val manageResource: ManageResource
) : CreateBoardResult.Mapper<CreateBoardUiState> {

    override fun mapSuccess(): CreateBoardUiState = CreateBoardUiState.Success

    override fun mapError(message: String): CreateBoardUiState = CreateBoardUiState.Error(message)

    override fun mapAlreadyExists(): CreateBoardUiState = CreateBoardUiState.AlreadyExists(
        message = manageResource.string(R.string.board_with_this_already_exists)
    )
}