//package com.github.radlance.kanbanboards.board.create.data
//
//import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
//import com.github.radlance.kanbanboards.board.create.domain.CreateBoardRepository
//import com.github.radlance.kanbanboards.board.create.domain.CreateBoardResult
//import com.github.radlance.kanbanboards.boards.domain.Board
//import com.github.radlance.kanbanboards.common.BaseTest
//import com.github.radlance.kanbanboards.common.data.RemoteUsersRepository
//import com.github.radlance.kanbanboards.common.domain.SearchUsersResult
//import com.github.radlance.kanbanboards.common.domain.User
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class CreateBoardRepositoryTest : BaseTest() {
//
//    private lateinit var manageResource: TestManageResource
//    private lateinit var boardsRemoteDataSource: TestBoardsRemoteDataSource
//    private lateinit var createBoardRemoteDataSource: TestCreateBoardRemoteDataSource
//
//    private lateinit var usersRemoteDataSource: TestUsersRemoteDataSource
//    private lateinit var repository: CreateBoardRepository
//
//    @Before
//    fun setup() {
//        boardsRemoteDataSource = TestBoardsRemoteDataSource()
//        createBoardRemoteDataSource = TestCreateBoardRemoteDataSource()
//        manageResource = TestManageResource()
//        usersRemoteDataSource = TestUsersRemoteDataSource()
//
//        repository = RemoteCreateBoardRepository(
//            boardsRemoteDataSource = boardsRemoteDataSource,
//            createBoardRemoteDataSource = createBoardRemoteDataSource,
//            usersRepository = RemoteUsersRepository(usersRemoteDataSource),
//            manageResource = manageResource
//        )
//    }
//
//    @Test
//    fun test_create_board_already_exists() = runBlocking {
//        boardsRemoteDataSource.makeExpectedMyBoards(
//            myBoards = listOf(Board.My(id = "123", name = "my first board"))
//        )
//
//        manageResource.makeExpectedString(expected = "board with this name already exists")
//
//        assertEquals(
//            CreateBoardResult.AlreadyExists(message = "board with this name already exists"),
//            repository.createBoard(name = "my first board", memberIds = listOf("1", "2", "3"))
//        )
//
//        assertEquals(0, createBoardRemoteDataSource.createBoardCalledCount)
//        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
//        assertEquals(1, manageResource.stringCalledCount)
//    }
//
//    @Test
//    fun test_create_board_error_with_message() = runBlocking {
//        createBoardRemoteDataSource.makeExpectedCreateBoardException(
//            expected = IllegalStateException("no response")
//        )
//
//        assertEquals(
//            CreateBoardResult.Error(message = "no response"),
//            repository.createBoard(name = "my board", memberIds = listOf("1", "2", "3"))
//        )
//
//        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
//        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
//        assertEquals(0, manageResource.stringCalledCount)
//    }
//
//    @Test
//    fun test_create_board_error_without_message() = runBlocking {
//        manageResource.makeExpectedString(expected = "error")
//
//        createBoardRemoteDataSource.makeExpectedCreateBoardException(
//            expected = IllegalStateException()
//        )
//
//        assertEquals(
//            CreateBoardResult.Error(message = "error"),
//            repository.createBoard(name = "my board", memberIds = listOf("1", "2", "3"))
//        )
//
//        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
//        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
//        assertEquals(1, manageResource.stringCalledCount)
//    }
//
//    @Test
//    fun test_create_board_success() = runBlocking {
//        assertEquals(
//            CreateBoardResult.Success(
//                boardInfo = BoardInfo(id = "", name = "another one my board", isMyBoard = true)
//            ),
//            repository.createBoard(name = "another one my board", memberIds = listOf("1", "2", "3"))
//        )
//
//        assertEquals(1, createBoardRemoteDataSource.createBoardCalledCount)
//        assertEquals(1, boardsRemoteDataSource.boardsCalledCount)
//    }
//
//    @Test
//    fun test_users_success() = runBlocking {
//        usersRemoteDataSource.makeExpectedUsers(
//            listOf(User(id = "test id", email = "test email", name = "test name"))
//        )
//        assertEquals(
//            SearchUsersResult.Success(
//                listOf(User(id = "test id", email = "test email", name = "test name"))
//            ),
//            repository.users().first()
//        )
//        assertEquals(1, usersRemoteDataSource.usersCalledCount)
//    }
//
//    @Test
//    fun test_users_error() = runBlocking {
//        usersRemoteDataSource.makeExpectedUsersException(IllegalStateException("bad request"))
//        assertEquals(
//            0,
//            repository.users().toList().size
//        )
//        assertEquals(1, usersRemoteDataSource.usersCalledCount)
//    }
//
//    private class TestCreateBoardRemoteDataSource : CreateBoardRemoteDataSource {
//
//        var createBoardCalledCount = 0
//        private var createBoardException: Exception? = null
//        fun makeExpectedCreateBoardException(expected: Exception) {
//            createBoardException = expected
//        }
//
//        override fun createBoard(name: String, memberIds: List<String>): BoardInfo {
//            createBoardCalledCount++
//            createBoardException?.let { throw it }
//            return BoardInfo(
//                id = "",
//                name = name,
//                isMyBoard = true
//            )
//        }
//    }
//}