package com.github.radlance.kanbanboards.board.settings.presentation

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.common.domain.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class HandleBoardSettingsTest {

    private lateinit var handle: HandleBoardSettings

    @Before
    fun setup() {
        handle = HandleBoardSettings.Base()
    }

    @Test
    fun test_board_settings_state() {
        assertEquals(BoardSettingsUiState.Loading, handle.boardSettingsUiState.value)
        handle.saveBoardSettingsUiState(BoardSettingsUiState.Error("board settings error"))
        assertEquals(
            BoardSettingsUiState.Error("board settings error"),
            handle.boardSettingsUiState.value
        )
        handle.saveBoardSettingsUiState(
            BoardSettingsUiState.Success(
                users = listOf(User(id = "id", email = "test@email.com", name = "name")),
                members = listOf(
                    BoardUser(
                        id = "boardMemberId",
                        userId = "userId",
                        email = "email@test.com",
                        name = "name2"
                    )
                ),
                invited = listOf(
                    BoardUser(
                        id = "invitedMemberId1",
                        userId = "invitedId1",
                        email = "invited@gmail.com",
                        name = "invited"
                    )
                )
            )
        )
        assertEquals(
            BoardSettingsUiState.Success(
                users = listOf(User(id = "id", email = "test@email.com", name = "name")),
                members = listOf(
                    BoardUser(
                        id = "boardMemberId",
                        userId = "userId",
                        email = "email@test.com",
                        name = "name2"
                    )
                ),
                invited = listOf(
                    BoardUser(
                        id = "invitedMemberId1",
                        userId = "invitedId1",
                        email = "invited@gmail.com",
                        name = "invited"
                    )
                )
            ),
            handle.boardSettingsUiState.value
        )
    }

    @Test
    fun test_settings_board_state() {
        assertEquals(SettingsBoardUiState.Loading, handle.settingsBoardUiState.value)
        handle.saveSettingsBoardUiState(SettingsBoardUiState.Error("settings board error"))
        assertEquals(
            SettingsBoardUiState.Error("settings board error"),
            handle.settingsBoardUiState.value
        )
        handle.saveSettingsBoardUiState(
            SettingsBoardUiState.Success(
                boardInfo = BoardInfo(
                    id = "id",
                    name = "name",
                    isMyBoard = false,
                    owner = "owner"
                )
            )
        )
        assertEquals(
            SettingsBoardUiState.Success(
                boardInfo = BoardInfo(
                    id = "id",
                    name = "name",
                    isMyBoard = false,
                    owner = "owner"
                )
            ),
            handle.settingsBoardUiState.value
        )
        handle.saveSettingsBoardUiState(SettingsBoardUiState.NotExists)
        assertEquals(SettingsBoardUiState.NotExists, handle.settingsBoardUiState.value)
    }

    @Test
    fun test_settings_field_state_initial_value() {
        assertEquals(SettingsFieldState(), handle.settingsFieldState.value)
    }

    @Test
    fun test_board_name_state() {
        assertEquals(UpdateBoardNameUiState.Initial, handle.updateBoardNameUiState.value)
        handle.saveUpdateBoardNameUiState(UpdateBoardNameUiState.Success)
        assertEquals(UpdateBoardNameUiState.Success, handle.updateBoardNameUiState.value)
        handle.saveUpdateBoardNameUiState(UpdateBoardNameUiState.Error("loading error"))
        assertEquals(
            UpdateBoardNameUiState.Error("loading error"),
            handle.updateBoardNameUiState.value
        )
        handle.saveUpdateBoardNameUiState(UpdateBoardNameUiState.AlreadyExists("already exists"))
        assertEquals(
            UpdateBoardNameUiState.AlreadyExists("already exists"),
            handle.updateBoardNameUiState.value
        )
    }
}