package com.github.radlance.board.settings.presentation

import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.board.settings.domain.BoardSettingsResult
import com.github.radlance.board.settings.domain.UpdateBoardNameResult
import javax.inject.Inject

interface BoardSettingsMapperFacade {

    fun mapBoardSettingsResult(boardSettingsResult: BoardSettingsResult): BoardSettingsUiState

    fun mapBoardResult(boardResult: BoardResult): SettingsBoardUiState

    fun mapUpdateBoardNameResult(boardNameResult: UpdateBoardNameResult): UpdateBoardNameUiState

    class Base @Inject constructor(
        private val boardSettingsMapper: BoardSettingsResult.Mapper<BoardSettingsUiState>,
        private val settingsBoardMapper: BoardResult.Mapper<SettingsBoardUiState>,
        private val updateBoardNameMapper: UpdateBoardNameResult.Mapper<UpdateBoardNameUiState>
    ) : BoardSettingsMapperFacade {

        override fun mapBoardSettingsResult(
            boardSettingsResult: BoardSettingsResult
        ): BoardSettingsUiState = boardSettingsResult.map(boardSettingsMapper)

        override fun mapBoardResult(boardResult: BoardResult) = boardResult.map(settingsBoardMapper)

        override fun mapUpdateBoardNameResult(boardNameResult: UpdateBoardNameResult) =
            boardNameResult.map(updateBoardNameMapper)
    }
}