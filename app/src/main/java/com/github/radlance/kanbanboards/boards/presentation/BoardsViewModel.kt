package com.github.radlance.kanbanboards.boards.presentation

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val boardMapper: Board.Mapper<BoardUi>,
    boardsRepository: BoardsRepository,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val boards: StateFlow<List<BoardUi>> = boardsRepository.boards().map { boardList ->
        boardList.map { it.map(boardMapper) }
    }.stateInViewModel(initialValue = emptyList())
}