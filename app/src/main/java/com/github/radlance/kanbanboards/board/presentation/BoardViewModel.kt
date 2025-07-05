package com.github.radlance.kanbanboards.board.presentation

import androidx.lifecycle.viewModelScope
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val handleBoard: HandleBoard,
    private val boardResultMapper: BoardResult.Mapper<BoardUiState>,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val boardUiState = handleBoard.boardUiState()

    // TODO useCase
    fun fetchBoard(boardInfo: BoardInfo) {
        boardRepository.loadBoard(boardInfo.id).map {
            it.map(boardResultMapper)
        }.onStart {
            handleBoard.saveBoardUiState(BoardUiState.Success(boardInfo))
        }.onEach {
            handleBoard.saveBoardUiState(it)
        }.launchIn(viewModelScope)
    }
}