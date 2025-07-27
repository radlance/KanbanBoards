package com.github.radlance.kanbanboards.board.create.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleCreateBoardTest {

    private lateinit var handle: HandleCreateBoard

    @Before
    fun setup() {
        handle = HandleCreateBoard.Base()
    }

    @Test
    fun test_create_board_state() {
        assertEquals(CreateBoardUiState.CanNotCreate, handle.createBoardFieldState.value)
        handle.saveCreateBoardFieldState(CreateBoardUiState.Loading)
        assertEquals(CreateBoardUiState.Loading, handle.createBoardFieldState.value)
        handle.saveCreateBoardFieldState(CreateBoardUiState.CanCreate)
        assertEquals(CreateBoardUiState.CanCreate, handle.createBoardFieldState.value)
        handle.saveCreateBoardFieldState(
            CreateBoardUiState.Success(
                BoardInfo(id = "123", name = "some board123", isMyBoard = false)
            )
        )
        assertEquals(
            CreateBoardUiState.Success(
                BoardInfo(id = "123", name = "some board123", isMyBoard = false)
            ),
            handle.createBoardFieldState.value
        )
        handle.saveCreateBoardFieldState(
            CreateBoardUiState.AlreadyExists(message = "already exists")
        )
        assertEquals(
            CreateBoardUiState.AlreadyExists(message = "already exists"),
            handle.createBoardFieldState.value
        )

        handle.saveCreateBoardFieldState(CreateBoardUiState.Error(message = "went wrong"))
        assertEquals(
            CreateBoardUiState.Error(message = "went wrong"),
            handle.createBoardFieldState.value
        )
    }

    @Test
    fun test_save_search_users_ui_state() {
        assertEquals(SearchUsersUiState.Loading, handle.searchUsersUiState.value)
        handle.saveSearchUsersUiState(
            SearchUsersUiState.Success(
                listOf(
                    CreateUserUi(
                        id = "testId", email = "testEmail", name = "testName", checked = false
                    )
                )
            )
        )
        assertEquals(
            SearchUsersUiState.Success(
                listOf(
                    CreateUserUi(
                        id = "testId", email = "testEmail", name = "testName", checked = false
                    )
                )
            ),
            handle.searchUsersUiState.value
        )
        handle.saveSearchUsersUiState(SearchUsersUiState.Error("some error"))
        assertEquals(SearchUsersUiState.Error("some error"), handle.searchUsersUiState.value)
    }
}