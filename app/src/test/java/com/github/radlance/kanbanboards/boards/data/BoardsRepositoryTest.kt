package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import com.github.radlance.kanbanboards.common.BaseTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BoardsRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestBoardsRemoteDataSource
    private lateinit var manageResource: TestManageResource

    private lateinit var boardsRepository: BoardsRepository

    @Before
    fun setup() {
        remoteDataSource = TestBoardsRemoteDataSource()
        manageResource = TestManageResource()

        boardsRepository = RemoteBoardsRepository(
            remoteDataSource = remoteDataSource,
            manageResource = manageResource
        )
    }


    @Test
    fun test_boards_all_empty() = runBlocking {
        assertEquals(0, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)

        val expected = BoardsResult.Success(
            listOf(
                Board.MyOwnBoardsTitle,
                Board.NoBoardsOfMyOwnHint,
                Board.OtherBoardsTitle,
                Board.HowToBeAddedToBoardHint
            )
        )

        assertEquals(expected, boardsRepository.boards().first())
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(1, remoteDataSource.otherBoardsCalledCount)
    }

    @Test
    fun test_my_boards_empty() = runBlocking {
        assertEquals(0, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)

        val otherBoards = listOf(
            Board.Other(
                id = "first",
                name = "first other board",
                owner = "first other board owner"
            ),

            Board.Other(
                id = "second",
                name = "second other board",
                owner = "second other board owner"
            )
        )

        remoteDataSource.makeExpectedOtherBoards(otherBoards = otherBoards)

        val expected = BoardsResult.Success(
            boards = buildList {
                add(Board.MyOwnBoardsTitle)
                add(Board.NoBoardsOfMyOwnHint)
                add(Board.OtherBoardsTitle)
                addAll(otherBoards)
            }
        )

        assertEquals(expected, boardsRepository.boards().first())
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(1, remoteDataSource.otherBoardsCalledCount)
    }

    @Test
    fun test_other_boards_empty() = runBlocking {
        assertEquals(0, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)

        val myBoards = listOf(
            Board.My(id = "first", name = "first my board"),
            Board.My(id = "second", name = "second my board")
        )

        remoteDataSource.makeExpectedMyBoards(myBoards = myBoards)

        val expected = BoardsResult.Success(
            boards = buildList {
                add(Board.MyOwnBoardsTitle)
                addAll(myBoards)
                add(Board.OtherBoardsTitle)
                add(Board.HowToBeAddedToBoardHint)
            }
        )

        assertEquals(expected, boardsRepository.boards().first())
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(1, remoteDataSource.otherBoardsCalledCount)
    }

    @Test
    fun test_boards_not_empty() = runBlocking {
        assertEquals(0, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)

        val otherBoards = listOf(
            Board.Other(
                id = "first",
                name = "first other board",
                owner = "first other board owner"
            ),

            Board.Other(
                id = "second",
                name = "second other board",
                owner = "second other board owner"
            )
        )

        val myBoards = listOf(
            Board.My(id = "first", name = "first my board"),
            Board.My(id = "second", name = "second my board")
        )

        remoteDataSource.makeExpectedOtherBoards(otherBoards = otherBoards)
        remoteDataSource.makeExpectedMyBoards(myBoards = myBoards)

        val expected = BoardsResult.Success(
            boards = buildList {
                add(Board.MyOwnBoardsTitle)
                addAll(myBoards)
                add(Board.OtherBoardsTitle)
                addAll(otherBoards)
            }
        )

        assertEquals(expected, boardsRepository.boards().first())
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(1, remoteDataSource.otherBoardsCalledCount)
    }

    @Test
    fun test_load_boards_throws_exception_with_message() = runBlocking {
        remoteDataSource.makeExpectedBoardsException(
            expected = IllegalStateException("server error")
        )

        val expected = BoardsResult.Error(message = "server error")
        val actual = boardsRepository.boards().first()
        assertEquals(expected, actual)
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)
    }

    @Test
    fun test_load_boards_throws_exception_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "error")
        remoteDataSource.makeExpectedBoardsException(
            expected = IllegalStateException()
        )

        val expected = BoardsResult.Error(message = "error")
        val actual = boardsRepository.boards().first()
        assertEquals(expected, actual)
        assertEquals(1, manageResource.stringCalledCount)
        assertEquals(1, remoteDataSource.boardsCalledCount)
        assertEquals(0, remoteDataSource.otherBoardsCalledCount)
    }
}