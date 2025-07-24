package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BoardSettingsViewModel @Inject constructor(
    private val boardSettingsRepository: BoardSettingsRepository,
    private val mapper: BoardSettingsResult.Mapper<BoardSettingsUiState>,
    private val handleBoardSettings: HandleBoardSettings,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val boardSettingsUiState = handleBoardSettings.boardSettingsUiState

    fun fetchBoardSettings(boardId: String, ownerId: String) {
        boardSettingsRepository.boardSettings(boardId, ownerId).map {
            it.map(mapper)
        }.onEach {
            handleBoardSettings.saveBoardSettingsUiState(it)
        }.launchInViewModel()
    }
}