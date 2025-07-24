package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import javax.inject.Inject

interface BoardSettingsMapperFacade {

    fun mapBoardSettingsResult(boardSettingsResult: BoardSettingsResult): BoardSettingsUiState

    fun mapBoardResult(boardResult: BoardResult): SettingsBoardUiState

    class Base @Inject constructor(
        private val boardSettingsMapper: BoardSettingsResult.Mapper<BoardSettingsUiState>,
        private val boardResultMapper: BoardResult.Mapper<SettingsBoardUiState>
    ) : BoardSettingsMapperFacade {

        override fun mapBoardSettingsResult(
            boardSettingsResult: BoardSettingsResult
        ): BoardSettingsUiState = boardSettingsResult.map(boardSettingsMapper)

        override fun mapBoardResult(boardResult: BoardResult) = boardResult.map(boardResultMapper)
    }
}