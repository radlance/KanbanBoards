package com.github.radlance.kanbanboards.boards.presentation

import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import com.github.radlance.kanbanboards.core.presentation.BaseViewModel
import com.github.radlance.kanbanboards.core.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val boardsMapper: BoardsResult.Mapper<BoardsUiState>,
    boardsRepository: BoardsRepository,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val boards: StateFlow<BoardsUiState> = boardsRepository.boards().map { boardsResult ->
        boardsResult.map(boardsMapper)
    }.stateInViewModel(initialValue = BoardsUiState.Loading)
}