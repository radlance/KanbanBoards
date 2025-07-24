package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import javax.inject.Inject

class SettingsBoardMapper @Inject constructor() : BoardResult.Mapper<SettingsBoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo) = SettingsBoardUiState.Success(boardInfo)

    override fun mapError(message: String) = SettingsBoardUiState.Error(message)
}