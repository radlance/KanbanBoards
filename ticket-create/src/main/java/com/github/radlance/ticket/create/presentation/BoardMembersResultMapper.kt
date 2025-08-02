package com.github.radlance.ticket.create.presentation

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.User
import javax.inject.Inject

class BoardMembersResultMapper @Inject constructor() :
    BoardMembersResult.Mapper<BoardMembersUiStateCreate> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateCreate =
        BoardMembersUiStateCreate.Success(members)

    override fun mapError(message: String): BoardMembersUiStateCreate =
        BoardMembersUiStateCreate.Error(message)
}