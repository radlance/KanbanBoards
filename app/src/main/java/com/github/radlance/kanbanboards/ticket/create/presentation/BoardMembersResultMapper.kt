package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import javax.inject.Inject

class BoardMembersResultMapper @Inject constructor(): BoardMembersResult.Mapper<BoardMembersUiState> {

    override fun mapSuccess(members: List<BoardMember>): BoardMembersUiState =
        BoardMembersUiState.Success(members)

    override fun mapError(message: String): BoardMembersUiState = BoardMembersUiState.Error(message)
}