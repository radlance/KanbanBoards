package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import javax.inject.Inject

class BoardMembersResultMapper @Inject constructor(): BoardMembersResult.Mapper<BoardMembersUiStateCreate> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateCreate =
        BoardMembersUiStateCreate.Success(members)

    override fun mapError(message: String): BoardMembersUiStateCreate = BoardMembersUiStateCreate.Error(message)
}