package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.User
import javax.inject.Inject

internal class BoardMembersResultMapper @Inject constructor() :
    BoardMembersResult.Mapper<BoardMembersUiStateCreate> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateCreate =
        BoardMembersUiStateCreate.Success(members)

    override fun mapError(message: String): BoardMembersUiStateCreate =
        BoardMembersUiStateCreate.Error(message)
}