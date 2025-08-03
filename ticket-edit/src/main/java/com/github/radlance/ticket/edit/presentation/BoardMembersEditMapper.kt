package com.github.radlance.ticket.edit.presentation

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.User
import javax.inject.Inject

internal class BoardMembersEditMapper @Inject constructor() :
    BoardMembersResult.Mapper<BoardMembersUiStateEdit> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Success(members)


    override fun mapError(message: String): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Error(message)
}