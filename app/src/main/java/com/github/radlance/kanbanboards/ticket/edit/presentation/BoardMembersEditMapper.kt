package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.common.domain.BoardMembersResult
import com.github.radlance.common.domain.User
import javax.inject.Inject

class BoardMembersEditMapper @Inject constructor() :
    BoardMembersResult.Mapper<BoardMembersUiStateEdit> {

    override fun mapSuccess(members: List<User>): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Success(members)


    override fun mapError(message: String): BoardMembersUiStateEdit =
        BoardMembersUiStateEdit.Error(message)
}