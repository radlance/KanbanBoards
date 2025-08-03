package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.core.domain.User
import javax.inject.Inject

internal class BoardSettingsMapper @Inject constructor() :
    BoardSettingsResult.Mapper<BoardSettingsUiState> {

    override fun mapSuccess(
        users: List<User>,
        members: List<BoardUser>,
        invited: List<BoardUser>
    ): BoardSettingsUiState {
        return BoardSettingsUiState.Success(users, members, invited)
    }

    override fun mapError(message: String) = BoardSettingsUiState.Error(message)
}