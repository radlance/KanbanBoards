package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateBoardViewModel @Inject constructor(
    private val createBoardRepository: CreateBoardRepository,
    private val handleCreateBoard: HandleCreateBoard,
    private val mapper: CreateBoardResult.Mapper<CreateBoardUiState>,
    runAsync: RunAsync
) : BaseViewModel(runAsync), CreateBoardActions {

    val createBoardUiState = handleCreateBoard.createBoardUiState

    override fun checkBoard(name: String) {
        handleCreateBoard.saveCreateBoardUiState(
            if (name.trim().length >= 3) {
                CreateBoardUiState.CanCreate
            } else CreateBoardUiState.CanNotCreate
        )
    }

    override fun createBoard(name: String) {
        handleCreateBoard.saveCreateBoardUiState(CreateBoardUiState.Loading)

        handle(background = { createBoardRepository.createBoard(name) }) {
            handleCreateBoard.saveCreateBoardUiState(it.map(mapper))
        }
    }
}

interface CreateBoardActions {

    fun checkBoard(name: String)

    fun createBoard(name: String)
}