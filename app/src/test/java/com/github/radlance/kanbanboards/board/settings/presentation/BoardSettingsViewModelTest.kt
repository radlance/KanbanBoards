package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
                settingsBoardMapper = SettingsBoardMapper()
            ),
            handleBoardSettings = handle,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(SettingsBoardUiState.Loading, viewModel.boardUiState.value)
        assertEquals(BoardSettingsUiState.Loading, viewModel.boardSettingsUiState.value)
        assertEquals(1, handle.settingsBoardUiStateCalledCount)
        assertEquals(1, handle.boardSettingsUiStateCalledCount)
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
                    BoardMember(
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
                    BoardMember(
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
                    BoardMember(
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
        viewModel.addUserToBoard(boardId = "123", userId = "321")
        assertEquals(1, repository.addUserToBoardCalledList.size)
        assertEquals(Pair("123", "321"), repository.addUserToBoardCalledList[0])
    }

    @Test
    fun test_delete_user_from_board() {
        viewModel.deleteUserFromBoard(boardMemberId = "000")
        assertEquals(1, repository.deleteUserFromBoardCalledList.size)
        assertEquals("000", repository.deleteUserFromBoardCalledList[0])
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

        fun makeExpectedBoardResult(boardResult: BoardResult) {
            this.boardResult.value = boardResult
        }

        fun makeExpectedBoardSettingsResult(boardSettingsResult: BoardSettingsResult) {
            this.boardSettingsResult.value = boardSettingsResult
        }

        override fun board(boardId: String): Flow<BoardResult> {
            boardCalledList.add(boardId)
            return boardResult
        }

        override fun boardSettings(boardId: String): Flow<BoardSettingsResult> {
            boardSettingsCalledList.add(boardId)
            return boardSettingsResult
        }

        override suspend fun addUserToBoard(boardId: String, userId: String) {
            addUserToBoardCalledList.add(Pair(boardId, userId))
        }

        override suspend fun deleteUserFromBoard(boardMemberId: String) {
            deleteUserFromBoardCalledList.add(boardMemberId)
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
    }
}