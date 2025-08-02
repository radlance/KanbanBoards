package com.github.radlance.board.settings.domain

import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.core.domain.BoardInfo
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface BoardSettingsRepository {

    fun board(boardId: String): Flow<BoardResult>

    fun boardSettings(boardId: String): Flow<BoardSettingsResult>

    fun inviteUserToBoard(boardId: String, userId: String, sendDate: ZonedDateTime)

    fun deleteUserFromBoard(boardMemberId: String)

    fun rollbackInvitation(invitedMemberId: String)

    suspend fun updateBoardName(boardInfo: BoardInfo): UpdateBoardNameResult

    suspend fun deleteBoard(boardId: String)
}