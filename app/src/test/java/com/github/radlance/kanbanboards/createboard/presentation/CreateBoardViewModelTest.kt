package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import com.github.radlance.kanbanboards.createboard.domain.SearchUsersResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateBoardViewModelTest : BaseTest() {

    private lateinit var repository: TestCreateBoardRepository
    private lateinit var handle: TestHandleCreateBoard

    private lateinit var viewModel: CreateBoardViewModel

    @Before
    fun setup() {
        repository = TestCreateBoardRepository()
        handle = TestHandleCreateBoard()

        viewModel = CreateBoardViewModel(
            createBoardRepository = repository,
            handleCreateBoard = handle,
            facade = CreateBoardMapperFacade.Base(
                createBoardMapper = CreateBoardResultMapper(),
                searchUsersMapper = SearchUsersResultMapper()
            ),
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(1, handle.createBoardUiStateCalledCount)
    }

    @Test
    fun test_check_board_short_name() {
        viewModel.checkBoard(name = "go")
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(1, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanNotCreate,
            handle.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(1, handle.createBoardUiStateCalledCount)

        viewModel.checkBoard(name = "  it    ")
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(2, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanNotCreate,
            handle.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, handle.createBoardUiStateCalledCount)
    }

    @Test
    fun test_check_board_valid_name() {
        viewModel.checkBoard(name = "test name")
        assertEquals(CreateBoardUiState.CanCreate, viewModel.createBoardUiState.value)
        assertEquals(1, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanCreate,
            handle.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(1, handle.createBoardUiStateCalledCount)
    }

    @Test
    fun test_create_exists_board() {
        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.AlreadyExists(message = "board already exists")
        )
        viewModel.createBoard(name = "first board", boardMembers = emptyList())

        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "board already exists"),
            viewModel.createBoardUiState.value
        )
        assertEquals(2, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            handle.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "board already exists"),
            handle.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, repository.createBoardCalledCount)
    }

    @Test
    fun test_create_new_board() {
        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.Error(message = "no internet connection")
        )
        viewModel.createBoard(name = "new board", boardMembers = emptyList())
        assertEquals(
            CreateBoardUiState.Error(message = "no internet connection"),
            viewModel.createBoardUiState.value
        )

        assertEquals(2, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            handle.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(
            CreateBoardUiState.Error(message = "no internet connection"),
            handle.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, repository.createBoardCalledCount)

        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.Success(
                BoardInfo(id = "", name = "second board", isMyBoard = true)
            )
        )
        viewModel.createBoard(name = "second board", boardMembers = emptyList())
        assertEquals(4, handle.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            handle.saveCreateBoardUiStateCalledList[2]
        )
        assertEquals(
            CreateBoardUiState.Success(
                BoardInfo(id = "", name = "second board", isMyBoard = true)
            ),
            handle.saveCreateBoardUiStateCalledList[3]
        )
        assertEquals(2, repository.createBoardCalledCount)
    }

    @Test
    fun test_collect_uses() {
        repository.makeExpectedSearchUsersResult(SearchUsersResult.Error("server error"))
        viewModel.fetchUsers()
        assertEquals(SearchUsersUiState.Error("server error"), viewModel.searchUsersUiState.value)
        assertEquals(1, repository.usersCalledCount)
        assertEquals(1, handle.searchUsersUiStateCalledCount)
        assertEquals(1, handle.saveSearchUsersUiStateCalledList.size)
        assertEquals(
            SearchUsersUiState.Error("server error"),
            handle.saveSearchUsersUiStateCalledList[0]
        )

        repository.makeExpectedSearchUsersResult(
            SearchUsersResult.Success(
                users = listOf(User(id = "123", name = "test name", email = "test email"))
            )
        )
        assertEquals(
            SearchUsersUiState.Success(
                users = listOf(
                    CreateUserUi(
                        id = "123", name = "test name", email = "test email", checked = false
                    )
                )
            ),
            viewModel.searchUsersUiState.value
        )
        assertEquals(1, repository.usersCalledCount)
        assertEquals(1, handle.searchUsersUiStateCalledCount)
        assertEquals(2, handle.saveSearchUsersUiStateCalledList.size)
        assertEquals(
            SearchUsersUiState.Success(
                users = listOf(
                    CreateUserUi(
                        id = "123", name = "test name", email = "test email", checked = false
                    )
                )
            ),
            handle.saveSearchUsersUiStateCalledList[1]
        )
    }

    @Test
    fun test_reset_board_ui_state() {
        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.AlreadyExists(message = "board already exists")
        )
        viewModel.createBoard(
            name = "first board",
            boardMembers = listOf(
                CreateUserUi(id = "id", name = "name", email = "email", checked = true)
            )
        )

        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "board already exists"),
            viewModel.createBoardUiState.value
        )

        viewModel.resetBoardUiState()

        assertEquals(
            CreateBoardUiState.CanNotCreate,
            viewModel.createBoardUiState.value
        )
    }

    @Test
    fun test_switch() {
        repository.makeExpectedSearchUsersResult(
            SearchUsersResult.Success(
                users = listOf(User(id = "123", name = "test name", email = "test email"))
            )
        )
        viewModel.fetchUsers()
        assertEquals(
            SearchUsersUiState.Success(
                users = listOf(
                    CreateUserUi(
                        id = "123", name = "test name", email = "test email", checked = false
                    )
                )
            ),
            viewModel.searchUsersUiState.value
        )

        viewModel.switch(
            userId = "123",
            users = listOf(
                CreateUserUi(
                    id = "123", name = "test name", email = "test email", checked = false
                )
            )
        )

        assertEquals(
            SearchUsersUiState.Success(
                users = listOf(
                    CreateUserUi(
                        id = "123", name = "test name", email = "test email", checked = true
                    )
                )
            ),
            viewModel.searchUsersUiState.value
        )

        viewModel.switch(
            userId = "0",
            users = listOf(
                CreateUserUi(
                    id = "123", name = "test name", email = "test email", checked = true
                )
            )
        )

        assertEquals(
            SearchUsersUiState.Success(
                users = listOf(
                    CreateUserUi(
                        id = "123", name = "test name", email = "test email", checked = true
                    )
                )
            ),
            viewModel.searchUsersUiState.value
        )
    }

    private class TestCreateBoardRepository : CreateBoardRepository {

        var createBoardCalledCount = 0
        private var createBoardResult: CreateBoardResult = CreateBoardResult.Success(
            boardInfo = BoardInfo(
                id = "",
                name = "initial createBoardResult",
                isMyBoard = true
            )
        )

        var usersCalledCount = 0
        private val searchUsersResult = MutableStateFlow<SearchUsersResult>(
            SearchUsersResult.Error("initial state")
        )

        fun makeExpectedCreateBoardResult(result: CreateBoardResult) {
            createBoardResult = result
        }

        fun makeExpectedSearchUsersResult(searchUsersResult: SearchUsersResult) {
            this.searchUsersResult.value = searchUsersResult
        }

        override suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult {
            createBoardCalledCount++
            return createBoardResult
        }

        override fun users(): Flow<SearchUsersResult> {
            usersCalledCount++
            return searchUsersResult
        }
    }

    private class TestHandleCreateBoard : HandleCreateBoard {

        var createBoardUiStateCalledCount = 0
        val saveCreateBoardUiStateCalledList = mutableListOf<CreateBoardUiState>()
        private var boardUiState =
            MutableStateFlow<CreateBoardUiState>(CreateBoardUiState.CanNotCreate)

        var searchUsersUiStateCalledCount = 0
        val saveSearchUsersUiStateCalledList = mutableListOf<SearchUsersUiState>()
        private var usersUiState = MutableStateFlow<SearchUsersUiState>(SearchUsersUiState.Loading)

        override val createBoardUiState: StateFlow<CreateBoardUiState>
            get() {
                createBoardUiStateCalledCount++
                return boardUiState
            }

        override fun saveCreateBoardUiState(createBoardUiState: CreateBoardUiState) {
            saveCreateBoardUiStateCalledList.add(createBoardUiState)
            this.boardUiState.value = createBoardUiState
        }

        override val searchUsersUiState: StateFlow<SearchUsersUiState>
            get() {
                searchUsersUiStateCalledCount++
                return usersUiState
            }

        override fun saveSearchUsersUiState(searchUsersUiState: SearchUsersUiState) {
            saveSearchUsersUiStateCalledList.add(searchUsersUiState)
            usersUiState.value = searchUsersUiState
        }
    }
}