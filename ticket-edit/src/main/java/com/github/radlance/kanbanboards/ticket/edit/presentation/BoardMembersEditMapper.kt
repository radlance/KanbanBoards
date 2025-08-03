package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.User
import javax.inject.Inject

internal class BoardMembersEditMapper @Inject constructor() :
    BoardMembersResult.Mapper<BoardMembersUiStateEdit> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Success(members)


    override fun mapError(message: String): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Error(message)
}