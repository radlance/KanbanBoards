package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BoardSettingsViewModelTest : BaseTest() {

    private lateinit var repository: TestBoardSettingsRepository
    private lateinit var handle: TestHandleBoardSettings

    private lateinit var viewModel: BoardSettingsViewModel

    @Before
    fun setup() {
        repository = TestBoardSettingsRepository()
        handle = TestHandleBoardSettings()

        viewModel = BoardSettingsViewModel(
            boardSettingsRepository = repository,
            facade = BoardSettingsMapperFacade.Base(
                boardSettingsMapper = BoardSettingsMapper(),
                settingsBoardMapper = SettingsBoardMapper(),
                updateBoardNameMapper = UpdateBoardNameMapper()
            ),
            handleBoardSettings = handle,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(SettingsBoardUiState.Loading, viewModel.boardUiState.value)
        assertEquals(BoardSettingsUiState.Loading, viewModel.boardSettingsUiState.value)
        assertEquals(UpdateBoardNameUiState.Initial, viewModel.updateBoardNameUiState.value)
        assertEquals(
            SettingsFieldState(nameErrorMessage = "", buttonEnabled = true),
            viewModel.settingsFieldState.value
        )
        assertEquals(1, handle.settingsBoardUiStateCalledCount)
        assertEquals(1, handle.boardSettingsUiStateCalledCount)
        assertEquals(1, handle.updateBoardNameUiStateCalledCount)
    }

    @Test
    fun test_collect_board() {
        repository.makeExpectedBoardResult(BoardResult.Error(message = "error loading board"))
        viewModel.fetchBoard(
            boardInfo = BoardInfo(id = "test id", name = "test name", isMyBoard = true)
        )
        assertEquals(
            SettingsBoardUiState.Error(message = "error loading board"),
            viewModel.boardUiState.value
        )
        assertEquals(1, repository.boardCalledList.size)
        assertEquals("test id", repository.boardCalledList[0])
        assertEquals(2, handle.saveSettingsBoardUiStateCalledList.size)
        assertEquals(
            SettingsBoardUiState.Success(
                BoardInfo(
                    id = "test id",
                    name = "test name",
                    isMyBoard = true
                )
            ),
            handle.saveSettingsBoardUiStateCalledList[0]
        )
        assertEquals(
            SettingsBoardUiState.Error(message = "error loading board"),
            handle.saveSettingsBoardUiStateCalledList[1]
        )

        repository.makeExpectedBoardResult(
            BoardResult.Success(
                boardInfo = BoardInfo(id = "first", name = "example", isMyBoard = false)
            )
        )
        assertEquals(
            SettingsBoardUiState.Success(
                BoardInfo(id = "first", name = "example", isMyBoard = false)
            ),
            viewModel.boardUiState.value
        )
        assertEquals(1, repository.boardCalledList.size)
        assertEquals(3, handle.saveSettingsBoardUiStateCalledList.size)
        assertEquals(
            SettingsBoardUiState.Success(
                BoardInfo(id = "first", name = "example", isMyBoard = false)
            ),
            handle.saveSettingsBoardUiStateCalledList[2]
        )

        repository.makeExpectedBoardResult(BoardResult.NotExists)
        assertEquals(SettingsBoardUiState.NotExists, viewModel.boardUiState.value)
        assertEquals(1, repository.boardCalledList.size)
        assertEquals(4, handle.saveSettingsBoardUiStateCalledList.size)
        assertEquals(
            SettingsBoardUiState.NotExists,
            handle.saveSettingsBoardUiStateCalledList[3]
        )
    }

    @Test
    fun test_collect_board_settings() {
        repository.makeExpectedBoardSettingsResult(BoardSettingsResult.Error("settings error"))
        viewModel.fetchBoardSettings(boardId = "test boardId")

        assertEquals(
            BoardSettingsUiState.Error("settings error"),
            viewModel.boardSettingsUiState.value
        )
        assertEquals(1, repository.boardSettingsCalledList.size)
        assertEquals("test boardId", repository.boardSettingsCalledList[0])
        assertEquals(1, handle.saveBoardSettingsUiStateCalledList.size)
        assertEquals(
            BoardSettingsUiState.Error("settings error"),
            handle.saveBoardSettingsUiStateCalledList[0]
        )

        repository.makeExpectedBoardSettingsResult(
            BoardSettingsResult.Success(
                users = listOf(User(id = "userId1", email = "email@test.com", name = "name1")),
                members = listOf(
                    BoardUser(
                        boardMemberId = "boardMemberId1",
                        userId = "userId2",
                        email = "test@gmail.com",
                        name = "name2"
                    )
                )
            )
        )
        assertEquals(
            BoardSettingsUiState.Success(
                users = listOf(User(id = "userId1", email = "email@test.com", name = "name1")),
                members = listOf(
                    BoardUser(
                        boardMemberId = "boardMemberId1",
                        userId = "userId2",
                        email = "test@gmail.com",
                        name = "name2"
                    )
                )
            ), viewModel.boardSettingsUiState.value
        )
        assertEquals(1, repository.boardSettingsCalledList.size)
        assertEquals(2, handle.saveBoardSettingsUiStateCalledList.size)
        assertEquals(
            BoardSettingsUiState.Success(
                users = listOf(User(id = "userId1", email = "email@test.com", name = "name1")),
                members = listOf(
                    BoardUser(
                        boardMemberId = "boardMemberId1",
                        userId = "userId2",
                        email = "test@gmail.com",
                        name = "name2"
                    )
                )
            ),
            handle.saveBoardSettingsUiStateCalledList[1]
        )
    }

    @Test
    fun test_add_user_to_board() {
        viewModel.inviteUserToBoard(boardId = "123", userId = "321")
        assertEquals(1, repository.addUserToBoardCalledList.size)
        assertEquals(Pair("123", "321"), repository.addUserToBoardCalledList[0])
    }

    @Test
    fun test_delete_user_from_board() {
        viewModel.deleteUserFromBoard(boardMemberId = "000")
        assertEquals(1, repository.deleteUserFromBoardCalledList.size)
        assertEquals("000", repository.deleteUserFromBoardCalledList[0])
    }

    @Test
    fun test_short_board_name() {
        viewModel.checkBoard(name = "  ok   ")
        assertFalse(viewModel.settingsFieldState.value.buttonEnabled)
    }

    @Test
    fun test_valid_board_name() {
        viewModel.checkBoard("board name")
        assertTrue(viewModel.settingsFieldState.value.buttonEnabled)
    }

    @Test
    fun test_update_board_name() {
        repository.makeExpectedUpdateBoardNameResult(
            UpdateBoardNameResult.Error("update board name error")
        )
        viewModel.updateBoardName(
            boardInfo = BoardInfo(
                id = "id",
                name = "name",
                isMyBoard = false
            )
        )
        assertEquals(
            UpdateBoardNameUiState.Error("update board name error"),
            viewModel.updateBoardNameUiState.value
        )
        assertEquals(1, repository.updateBoardNameCalledList.size)
        assertEquals(1, handle.updateBoardNameUiStateCalledCount)
        assertEquals(
            BoardInfo(
                id = "id",
                name = "name",
                isMyBoard = false
            ),
            repository.updateBoardNameCalledList[0]
        )
        assertEquals(2, handle.saveUpdateBoardNameUiStateCalledList.size)
        assertEquals(UpdateBoardNameUiState.Loading, handle.saveUpdateBoardNameUiStateCalledList[0])
        assertEquals(
            UpdateBoardNameUiState.Error("update board name error"),
            handle.saveUpdateBoardNameUiStateCalledList[1]
        )

        repository.makeExpectedUpdateBoardNameResult(UpdateBoardNameResult.Success)
        viewModel.updateBoardName(
            BoardInfo(
                id = "id2",
                name = "name2",
                isMyBoard = false
            )
        )
        assertEquals(UpdateBoardNameUiState.Success, viewModel.updateBoardNameUiState.value)
        assertEquals(2, repository.updateBoardNameCalledList.size)
        assertEquals(
            BoardInfo(
                id = "id2",
                name = "name2",
                isMyBoard = false
            ),
            repository.updateBoardNameCalledList[1]
        )
        assertEquals(1, handle.updateBoardNameUiStateCalledCount)
        assertEquals(4, handle.saveUpdateBoardNameUiStateCalledList.size)
        assertEquals(UpdateBoardNameUiState.Loading, handle.saveUpdateBoardNameUiStateCalledList[2])
        assertEquals(UpdateBoardNameUiState.Success, handle.saveUpdateBoardNameUiStateCalledList[3])
    }

    @Test
    fun test_reset_board_ui_state() {
        viewModel.checkBoard(name = "  ok   ")
        assertFalse(viewModel.settingsFieldState.value.buttonEnabled)
        viewModel.resetBoardUiState()
        assertEquals(
            viewModel.settingsFieldState.value,
            SettingsFieldState(nameErrorMessage = "", buttonEnabled = true)
        )
    }

    @Test
    fun test_delete_board() {
        viewModel.deleteBoard(boardId = "boardId")
        assertEquals(1, repository.deleteBoardCalledList.size)
        assertEquals("boardId", repository.deleteBoardCalledList[0])
    }

    @Test
    fun test_set_board_name_error_message() {
        Assert.assertEquals("", viewModel.settingsFieldState.value.nameErrorMessage)
        viewModel.setBoardNameErrorMessage(message = "test message")
        Assert.assertEquals(
            "test message",
            viewModel.settingsFieldState.value.nameErrorMessage
        )
    }

    private class TestBoardSettingsRepository : BoardSettingsRepository {

        val boardCalledList = mutableListOf<String>()
        private val boardResult = MutableStateFlow<BoardResult>(BoardResult.Error("initial state"))

        val boardSettingsCalledList = mutableListOf<String>()
        private val boardSettingsResult = MutableStateFlow<BoardSettingsResult>(
            BoardSettingsResult.Error("initial state")
        )

        val addUserToBoardCalledList = mutableListOf<Pair<String, String>>()
        val deleteUserFromBoardCalledList = mutableListOf<String>()

        private var updateBoardNameResult: UpdateBoardNameResult =
            UpdateBoardNameResult.Error("initial state")

        val updateBoardNameCalledList = mutableListOf<BoardInfo>()

        val deleteBoardCalledList = mutableListOf<String>()

        fun makeExpectedBoardResult(boardResult: BoardResult) {
            this.boardResult.value = boardResult
        }

        fun makeExpectedBoardSettingsResult(boardSettingsResult: BoardSettingsResult) {
            this.boardSettingsResult.value = boardSettingsResult
        }

        fun makeExpectedUpdateBoardNameResult(updateBoardNameResult: UpdateBoardNameResult) {
            this.updateBoardNameResult = updateBoardNameResult
        }

        override fun board(boardId: String): Flow<BoardResult> {
            boardCalledList.add(boardId)
            return boardResult
        }

        override fun boardSettings(boardId: String): Flow<BoardSettingsResult> {
            boardSettingsCalledList.add(boardId)
            return boardSettingsResult
        }

        override suspend fun inviteUserToBoard(boardId: String, userId: String) {
            addUserToBoardCalledList.add(Pair(boardId, userId))
        }

        override suspend fun deleteUserFromBoard(boardMemberId: String) {
            deleteUserFromBoardCalledList.add(boardMemberId)
        }

        override suspend fun updateBoardName(boardInfo: BoardInfo): UpdateBoardNameResult {
            updateBoardNameCalledList.add(boardInfo)
            return updateBoardNameResult
        }

        override suspend fun deleteBoard(boardId: String) {
            deleteBoardCalledList.add(boardId)
        }
    }

    private class TestHandleBoardSettings : HandleBoardSettings {

        var settingsBoardUiStateCalledCount = 0
        private val settingsBoardUiStateMutable = MutableStateFlow<SettingsBoardUiState>(
            SettingsBoardUiState.Loading
        )
        val saveSettingsBoardUiStateCalledList = mutableListOf<SettingsBoardUiState>()

        var boardSettingsUiStateCalledCount = 0
        private val boardSettingsUiStateMutable = MutableStateFlow<BoardSettingsUiState>(
            BoardSettingsUiState.Loading
        )
        val saveBoardSettingsUiStateCalledList = mutableListOf<BoardSettingsUiState>()

        var updateBoardNameUiStateCalledCount = 0
        private val updateBoardNameUiStateMutable = MutableStateFlow<UpdateBoardNameUiState>(
            UpdateBoardNameUiState.Initial
        )
        val saveUpdateBoardNameUiStateCalledList = mutableListOf<UpdateBoardNameUiState>()

        private val settingsFieldStateMutable = MutableStateFlow(SettingsFieldState())
        override val settingsBoardUiState: StateFlow<SettingsBoardUiState>
            get() {
                settingsBoardUiStateCalledCount++
                return settingsBoardUiStateMutable
            }

        override fun saveSettingsBoardUiState(settingsBoardUiState: SettingsBoardUiState) {
            saveSettingsBoardUiStateCalledList.add(settingsBoardUiState)
            settingsBoardUiStateMutable.value = settingsBoardUiState
        }

        override val boardSettingsUiState: StateFlow<BoardSettingsUiState>
            get() {
                boardSettingsUiStateCalledCount++
                return boardSettingsUiStateMutable
            }

        override fun saveBoardSettingsUiState(boardSettingsUiState: BoardSettingsUiState) {
            saveBoardSettingsUiStateCalledList.add(boardSettingsUiState)
            boardSettingsUiStateMutable.value = boardSettingsUiState
        }

        override val settingsFieldState = settingsFieldStateMutable

        override val updateBoardNameUiState: StateFlow<UpdateBoardNameUiState>
            get() {
                updateBoardNameUiStateCalledCount++
                return updateBoardNameUiStateMutable
            }

        override fun saveUpdateBoardNameUiState(updateBoardNameUiState: UpdateBoardNameUiState) {
            saveUpdateBoardNameUiStateCalledList.add(updateBoardNameUiState)
            updateBoardNameUiStateMutable.value = updateBoardNameUiState
        }
    }
}