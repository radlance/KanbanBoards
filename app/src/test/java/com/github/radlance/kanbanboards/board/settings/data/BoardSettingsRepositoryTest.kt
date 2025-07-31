package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class BoardSettingsRepositoryTest : BaseTest() {

    private lateinit var usersRemoteDataSource: TestUsersRemoteDataSource
    private lateinit var boardRepository: TestBoardRepository
    private lateinit var remoteDataSource: TestBoardSettingsRemoteDataSource
    private lateinit var boardsRemoteDataSource: TestBoardsRemoteDataSource
    private lateinit var manageResource: TestManageResource

    private lateinit var repository: BoardSettingsRepository

    @Before
    fun setup() {
        usersRemoteDataSource = TestUsersRemoteDataSource()
        boardRepository = TestBoardRepository()
        remoteDataSource = TestBoardSettingsRemoteDataSource()
        boardsRemoteDataSource = TestBoardsRemoteDataSource()
        manageResource = TestManageResource()

        repository = RemoteBoardSettingsRepository(
            usersRemoteDataSource = usersRemoteDataSource,
            boardRepository = boardRepository,
            boardSettingsRemoteDataSource = remoteDataSource,
            boardsRemoteDataSource = boardsRemoteDataSource,
            manageResource = manageResource
        )
    }

    @Test
    fun test_add_user_to_board() = runBlocking {
        repository.inviteUserToBoard(boardId = "boardId", userId = "userId")
        assertEquals(1, remoteDataSource.addUserToBoardCalledList.size)
        assertEquals(Pair("boardId", "userId"), remoteDataSource.addUserToBoardCalledList[0])
    }

    @Test
    fun test_delete_user_from_board() = runBlocking {
        repository.deleteUserFromBoard(boardMemberId = "boardMemberId")
        assertEquals(1, remoteDataSource.deleteUserFromBoardCalledList.size)
        assertEquals("boardMemberId", remoteDataSource.deleteUserFromBoardCalledList[0])
    }

    @Test
    fun test_board_settings_success() = runBlocking {
        usersRemoteDataSource.makeExpectedUsers(
            users = listOf(
                User(id = "user id", email = "test@email.com", name = "user name")
            )
        )

        remoteDataSource.makeExpectedBoardMembers(
            boardUsers = listOf(
                BoardUser(
                    id = "123",
                    userId = "321",
                    email = "email@test.com",
                    name = "name"
                )
            )
        )

        assertEquals(
            BoardSettingsResult.Success(
                users = listOf(
                    User(id = "user id", email = "test@email.com", name = "user name")
                ),
                members = listOf(
                    BoardUser(
                        id = "123",
                        userId = "321",
                        email = "email@test.com",
                        name = "name"
                    )
                )
            ),
            repository.boardSettings(boardId = "boardId").first()
        )
        assertEquals(1, usersRemoteDataSource.usersCalledCount)
        assertEquals(1, remoteDataSource.boardMembersCalledList.size)
        assertEquals("boardId", remoteDataSource.boardMembersCalledList[0])
    }

    @Test
    fun test_board_settings_error() = runBlocking {
        remoteDataSource.makeExpectedBoardMembersException(IllegalStateException("error"))
        val actual = repository.boardSettings(boardId = "boardId").toList()
        assertEquals(emptyList<BoardSettingsResult>(), actual)
    }

    @Test
    fun test_update_board_name_success() = runBlocking {
        assertEquals(
            UpdateBoardNameResult.Success,
            repository.updateBoardName(BoardInfo(id = "123", name = "name", isMyBoard = false))
        )
    }

    @Test
    fun test_update_board_name_error_with_message() = runBlocking {
        remoteDataSource.makeExpectedUpdateBoardNameException(IllegalStateException("error"))
        assertEquals(
            UpdateBoardNameResult.Error("error"),
            repository.updateBoardName(BoardInfo(id = "123", name = "name", isMyBoard = false))
        )
    }

    @Test
    fun test_update_board_name_error_without_message() = runBlocking {
        manageResource.makeExpectedString("another error")
        remoteDataSource.makeExpectedUpdateBoardNameException(IllegalStateException())
        assertEquals(
            UpdateBoardNameResult.Error("another error"),
            repository.updateBoardName(BoardInfo(id = "123", name = "name", isMyBoard = false))
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_update_board_name_already_exists() = runBlocking {
        manageResource.makeExpectedString("already exists")
        boardsRemoteDataSource.makeExpectedMyBoards(
            myBoards = listOf(Board.My(id = "123", name = "test name"))
        )
        assertEquals(
            UpdateBoardNameResult.AlreadyExists("already exists"),
            repository.updateBoardName(BoardInfo(id = "123", name = "test name", isMyBoard = false))
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_board() = runBlocking {
        repository.deleteBoard(boardId = "last board")
        assertEquals(1, boardRepository.deleteBoardCalledList.size)
        assertEquals("last board", boardRepository.deleteBoardCalledList[0])
    }

    private class TestBoardSettingsRemoteDataSource : BoardSettingsRemoteDataSource {

        val addUserToBoardCalledList = mutableListOf<Pair<String, String>>()
        val deleteUserFromBoardCalledList = mutableListOf<String>()

        val boardMembersCalledList = mutableListOf<String>()
        private val boardMembers = MutableStateFlow<List<BoardUser>>(emptyList())
        private var boardMembersException: Exception? = null

        val updateBoardNameCalledList = mutableListOf<BoardInfo>()
        private var updateBoardNameException: Exception? = null

        fun makeExpectedBoardMembers(boardUsers: List<BoardUser>) {
            this.boardMembers.value = boardUsers
        }

        fun makeExpectedBoardMembersException(exception: Exception) {
            boardMembersException = exception
        }

        fun makeExpectedUpdateBoardNameException(exception: Exception) {
            updateBoardNameException = exception
        }

        override suspend fun inviteUserToBoard(boardId: String, userId: String) {
            addUserToBoardCalledList.add(Pair(boardId, userId))
        }

        override suspend fun deleteUserFromBoard(boardMemberId: String) {
            deleteUserFromBoardCalledList.add(boardMemberId)
        }

        override fun boardMembers(boardId: String): Flow<List<BoardUser>> = flow {
            boardMembersCalledList.add(boardId)
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }

        override suspend fun updateBoardName(boardInfo: BoardInfo) {
            updateBoardNameCalledList.add(boardInfo)
            updateBoardNameException?.let { throw it }
        }
    }
}