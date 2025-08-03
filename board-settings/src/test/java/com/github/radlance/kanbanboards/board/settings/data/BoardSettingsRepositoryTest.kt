package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.BaseBoardCoreTest
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.kanbanboards.core.domain.Board
import com.github.radlance.kanbanboards.core.domain.BoardInfo
import com.github.radlance.kanbanboards.core.domain.User
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class BoardSettingsRepositoryTest : BaseBoardCoreTest() {

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
            manageResource = manageResource,
            ignoreHandle = TestIgnoreHandle()
        )
    }

    @Test
    fun test_add_user_to_board() = runBlocking {
        repository.inviteUserToBoard(
            boardId = "boardId",
            userId = "userId",
            sendDate = ZonedDateTime.of(LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC"))
        )
        assertEquals(1, remoteDataSource.addUserToBoardCalledList.size)
        assertEquals(
            Triple(
                "boardId",
                "userId",
                ZonedDateTime.of(LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC"))
            ),
            remoteDataSource.addUserToBoardCalledList[0]
        )
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

        remoteDataSource.makeExpectedInvitedUsers(
            invitedUsers = listOf(
                BoardUser(
                    id = "456",
                    userId = "654",
                    email = "email2@gmail.com",
                    name = "name2"
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
                ),
                invited = listOf(
                    BoardUser(
                        id = "456",
                        userId = "654",
                        email = "email2@gmail.com",
                        name = "name2"
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

    @Test
    fun test_rollback_invitation() = runBlocking {
        repository.rollbackInvitation(invitedMemberId = "test member id")
        assertEquals(1, remoteDataSource.rollbackInvitationCalledList.size)
        assertEquals("test member id", remoteDataSource.rollbackInvitationCalledList[0])
    }

    private class TestBoardSettingsRemoteDataSource : BoardSettingsRemoteDataSource {

        val addUserToBoardCalledList = mutableListOf<Triple<String, String, ZonedDateTime>>()
        val deleteUserFromBoardCalledList = mutableListOf<String>()

        val boardMembersCalledList = mutableListOf<String>()
        private val boardMembers = MutableStateFlow<List<BoardUser>>(emptyList())
        private var boardMembersException: Exception? = null

        val updateBoardNameCalledList = mutableListOf<BoardInfo>()
        private var updateBoardNameException: Exception? = null

        val rollbackInvitationCalledList = mutableListOf<String>()

        private val invitedUsersCalledList = mutableListOf<String>()
        private val invitedUsers = MutableStateFlow<List<BoardUser>>(emptyList())

        fun makeExpectedBoardMembers(boardUsers: List<BoardUser>) {
            this.boardMembers.value = boardUsers
        }

        fun makeExpectedBoardMembersException(exception: Exception) {
            boardMembersException = exception
        }

        fun makeExpectedUpdateBoardNameException(exception: Exception) {
            updateBoardNameException = exception
        }

        fun makeExpectedInvitedUsers(invitedUsers: List<BoardUser>) {
            this.invitedUsers.value = invitedUsers
        }

        override fun inviteUserToBoard(
            boardId: String,
            userId: String,
            sendDate: ZonedDateTime
        ) {
            addUserToBoardCalledList.add(Triple(boardId, userId, sendDate))
        }

        override fun deleteUserFromBoard(boardMemberId: String) {
            deleteUserFromBoardCalledList.add(boardMemberId)
        }

        override fun rollbackInvitation(invitedMemberId: String) {
            rollbackInvitationCalledList.add(invitedMemberId)
        }

        override fun boardMembers(boardId: String): Flow<List<BoardUser>> = flow {
            boardMembersCalledList.add(boardId)
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }

        override fun invitedUsers(boardId: String): Flow<List<BoardUser>> = flow {
            invitedUsersCalledList.add(boardId)
            emitAll(invitedUsers)
        }

        override fun updateBoardName(boardInfo: BoardInfo) {
            updateBoardNameCalledList.add(boardInfo)
            updateBoardNameException?.let { throw it }
        }
    }
}