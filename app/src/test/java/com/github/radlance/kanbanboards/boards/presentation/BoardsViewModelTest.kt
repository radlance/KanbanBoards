package com.github.radlance.kanbanboards.boards.presentation

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import com.github.radlance.kanbanboards.common.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BoardsViewModelTest : BaseTest() {

    private lateinit var viewModel: BoardsViewModel
    private lateinit var boardsRepository: TestBoardsRepository

    @Before
    fun setup() {
        boardsRepository = TestBoardsRepository()

        viewModel = BoardsViewModel(
            boardsMapper = BoardsResultMapper(BoardMapper()),
            boardsRepository = boardsRepository,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_collect_boards() {
        boardsRepository.makeExpectedBoardsResult(
            BoardsResult.Success(
                boards = listOf(
                    Board.My(id = "test id", name = "test name"),
                    Board.Other(id = "test id", name = "test name", owner = "test owner"),
                    Board.MyOwnBoardsTitle,
                    Board.NoBoardsOfMyOwnHint,
                    Board.OtherBoardsTitle,
                    Board.HowToBeAddedToBoardHint
                )
            )
        )
        val expected = BoardsUiState.Success(
            boards = listOf(
                BoardUi.My(id = "test id", name = "test name"),
                BoardUi.Other(id = "test id", name = "test name", owner = "test owner"),
                BoardUi.MyOwnBoardsTitle,
                BoardUi.NoBoardsOfMyOwnHint,
                BoardUi.OtherBoardsTitle,
                BoardUi.HowToBeAddedToBoardHint
            )
        )

        assertEquals(expected, viewModel.boards.value)
        assertEquals(1, boardsRepository.boardsCalledCount)

        boardsRepository.makeExpectedBoardsResult(
            BoardsResult.Error(message = "loading boards error")
        )

        assertEquals(BoardsUiState.Error(message = "loading boards error"), viewModel.boards.value)
        assertEquals(1, boardsRepository.boardsCalledCount)
    }

    private class TestBoardsRepository : BoardsRepository {

        var boardsCalledCount = 0

        private val boardsResult = MutableStateFlow<BoardsResult>(
            BoardsResult.Error(message = "initial value")
        )

        fun makeExpectedBoardsResult(boardsResult: BoardsResult) {
            this.boardsResult.value = boardsResult
        }

        override fun boards(): Flow<BoardsResult> {
            boardsCalledCount++
            return boardsResult
        }
    }
}