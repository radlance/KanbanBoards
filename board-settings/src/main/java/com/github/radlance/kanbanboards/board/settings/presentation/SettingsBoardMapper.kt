package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.core.domain.BoardInfo
import javax.inject.Inject

internal class SettingsBoardMapper @Inject constructor() :
    BoardResult.Mapper<SettingsBoardUiState> {

    override fun mapSuccess(boardInfo: BoardInfo) = SettingsBoardUiState.Success(boardInfo)

    override fun mapError(message: String) = SettingsBoardUiState.Error(message)

    override fun mapNotExists(): SettingsBoardUiState = SettingsBoardUiState.NotExists
}