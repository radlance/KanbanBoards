package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import javax.inject.Inject

class BoardMembersResultMapper @Inject constructor(): BoardMembersResult.Mapper<BoardMembersUiState> {

    override fun mapSuccess(members: List<User>): BoardMembersUiState =
        BoardMembersUiState.Success(members)

    override fun mapError(message: String): BoardMembersUiState = BoardMembersUiState.Error(message)
}