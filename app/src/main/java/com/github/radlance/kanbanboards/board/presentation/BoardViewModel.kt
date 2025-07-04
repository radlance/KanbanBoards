package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {
    val board = boardRepository.loadBoard()
}