package com.github.radlance.kanbanboards.createboard.presentation

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateBoardViewModelTest : BaseTest() {

    private lateinit var repository: TestCreateBoardRepository
    private lateinit var viewModelWrapper: TestCreateBoardViewModelWrapper

    private lateinit var viewModel: CreateBoardViewModel

    @Before
    fun setup() {
        repository = TestCreateBoardRepository()
        viewModelWrapper = TestCreateBoardViewModelWrapper()

        viewModel = CreateBoardViewModel(
            createBoardRepository = repository,
            createBoardViewModelWrapper = viewModelWrapper,
            mapper = CreateBoardResultMapper(),
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(1, viewModelWrapper.createBoardUiStateCalledCount)
    }

    @Test
    fun test_check_board_short_name() {
        viewModel.checkBoard(name = "go")
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(1, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanNotCreate,
            viewModelWrapper.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(1, viewModelWrapper.createBoardUiStateCalledCount)

        viewModel.checkBoard(name = "  it    ")
        assertEquals(CreateBoardUiState.CanNotCreate, viewModel.createBoardUiState.value)
        assertEquals(2, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanNotCreate,
            viewModelWrapper.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, viewModelWrapper.createBoardUiStateCalledCount)
    }

    @Test
    fun test_check_board_valid_name() {
        viewModel.checkBoard(name = "test name")
        assertEquals(CreateBoardUiState.CanCreate, viewModel.createBoardUiState.value)
        assertEquals(1, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.CanCreate,
            viewModelWrapper.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(1, viewModelWrapper.createBoardUiStateCalledCount)
    }

    @Test
    fun test_create_exists_board() {
        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.AlreadyExists(message = "board already exists")
        )
        viewModel.createBoard(name = "first board")

        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "board already exists"),
            viewModel.createBoardUiState.value
        )
        assertEquals(2, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            viewModelWrapper.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "board already exists"),
            viewModelWrapper.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, repository.createBoardCalledCount)
    }

    @Test
    fun test_create_new_board() {
        repository.makeExpectedCreateBoardResult(
            CreateBoardResult.Error(message = "no internet connection")
        )
        viewModel.createBoard(name = "new board")
        assertEquals(
            CreateBoardUiState.Error(message = "no internet connection"),
            viewModel.createBoardUiState.value
        )

        assertEquals(2, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            viewModelWrapper.saveCreateBoardUiStateCalledList[0]
        )
        assertEquals(
            CreateBoardUiState.Error(message = "no internet connection"),
            viewModelWrapper.saveCreateBoardUiStateCalledList[1]
        )
        assertEquals(1, repository.createBoardCalledCount)

        repository.makeExpectedCreateBoardResult(CreateBoardResult.Success)
        viewModel.createBoard(name = "second board")
        assertEquals(4, viewModelWrapper.saveCreateBoardUiStateCalledList.size)
        assertEquals(
            CreateBoardUiState.Loading,
            viewModelWrapper.saveCreateBoardUiStateCalledList[2]
        )
        assertEquals(
            CreateBoardUiState.Success,
            viewModelWrapper.saveCreateBoardUiStateCalledList[3]
        )
        assertEquals(2, repository.createBoardCalledCount)
    }

    private class TestCreateBoardRepository : CreateBoardRepository {

        var createBoardCalledCount = 0
        private var createBoardResult: CreateBoardResult = CreateBoardResult.Success


        fun makeExpectedCreateBoardResult(result: CreateBoardResult) {
            createBoardResult = result
        }

        override suspend fun createBoard(name: String): CreateBoardResult {
            createBoardCalledCount++
            return createBoardResult
        }
    }

    private class TestCreateBoardViewModelWrapper : CreateBoardViewModelWrapper {

        var createBoardUiStateCalledCount = 0
        val saveCreateBoardUiStateCalledList = mutableListOf<CreateBoardUiState>()
        private var boardUiState =
            MutableStateFlow<CreateBoardUiState>(CreateBoardUiState.CanNotCreate)

        override fun createBoardUiState(): StateFlow<CreateBoardUiState> {
            createBoardUiStateCalledCount++
            return boardUiState
        }

        override fun saveCreateBoardUiState(boardUiState: CreateBoardUiState) {
            saveCreateBoardUiStateCalledList.add(boardUiState)
            this.boardUiState.value = boardUiState
        }
    }
}