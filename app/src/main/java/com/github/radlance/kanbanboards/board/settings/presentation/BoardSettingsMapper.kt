package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.common.domain.User
import javax.inject.Inject

class BoardSettingsMapper @Inject constructor() : BoardSettingsResult.Mapper<BoardSettingsUiState> {

    override fun mapSuccess(
        users: List<User>,
        members: List<User>
    ): BoardSettingsUiState = BoardSettingsUiState.Success(users, members)

    override fun mapError(message: String) = BoardSettingsUiState.Error(message)
}