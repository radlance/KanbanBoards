package com.github.radlance.kanbanboards.createboard.presentation

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