package com.github.radlance.board.settings.presentation

import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.core.domain.BoardInfo
import javax.inject.Inject

class SettingsBoardMapper @Inject constructor() : BoardResult.Mapper<SettingsBoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo) = SettingsBoardUiState.Success(boardInfo)

    override fun mapError(message: String) = SettingsBoardUiState.Error(message)

    override fun mapNotExists(): SettingsBoardUiState = SettingsBoardUiState.NotExists
}