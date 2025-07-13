package com.github.radlance.kanbanboards.createboard.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateBoardRepositoryTest : BaseTest() {

    private lateinit var manageResource: TestManageResource
    private lateinit var repository: CreateBoardRepository

    private lateinit var boardsRemoteDataSource: TestBoardsRemoteDataSource
    private lateinit var createBoardRemoteDataSource: TestCreateBoardRemoteDataSource

    @Before
    fun setup() {
        boardsRemoteDataSource = TestBoardsRemoteDataSource()
        createBoardRemoteDataSource = TestCreateBoardRemoteDataSource()
        manageResource = TestManageResource()

        repository = RemoteCreateBoardRepository(
            boardsRemoteDataSource = boardsRemoteDataSource,
            createBoardRemoteDataSource = createBoardRemoteDataSource,
            manageResource = manageResource
        )
    }

    @Test
    fun test_create_board_already_exists() = runBlocking {
        boardsRemoteDataSource.makeExpectedMyBoards(
            myBoards = listOf(Board.My(id = "123", name = "my first board"))
        )

        manageResource.makeExpectedString(expected = "board with this name already exists")

        assertEquals(
            CreateBoardResult.AlreadyExists(message = "board with this name already exists"),
            repository.createBoard(name = "my first board")
        )

        assertEquals(0, createBoardRemoteDataSource.createBoardCalledCount)
        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_create_board_error_with_message() = runBlocking {
        createBoardRemoteDataSource.makeExpectedCreateBoardException(
            expected = IllegalStateException("no response")
        )

        assertEquals(
            CreateBoardResult.Error(message = "no response"),
            repository.createBoard(name = "my board")
        )

        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_create_board_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "error")

        createBoardRemoteDataSource.makeExpectedCreateBoardException(
            expected = IllegalStateException()
        )

        assertEquals(
            CreateBoardResult.Error(message = "error"),
            repository.createBoard(name = "my board")
        )

        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_create_board_success() = runBlocking {
        assertEquals(
            CreateBoardResult.Success(
                boardInfo = BoardInfo(id = "", name = "another one my board", isMyBoard = true)
            ),
            repository.createBoard(name = "another one my board")
        )

        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
    }

    private class TestCreateBoardRemoteDataSource : CreateBoardRemoteDataSource {

        var createBoardCalledCount = 0

        private var createBoardException: Exception? = null


        fun makeExpectedCreateBoardException(expected: Exception) {
            createBoardException = expected
        }

        override suspend fun createBoard(name: String): BoardInfo {
            createBoardCalledCount++
            createBoardException?.let { throw it }
            return BoardInfo(
                id = "",
                name = name,
                isMyBoard = true
            )
        }
    }
}