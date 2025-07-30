package com.github.radlance.kanbanboards.board.settings.domain

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import kotlinx.coroutines.flow.Flow

interface BoardSettingsRepository {

    fun board(boardId: String): Flow<BoardResult>

    fun boardSettings(boardId: String): Flow<BoardSettingsResult>

    suspend fun inviteUserToBoard(boardId: String, userId: String)

    suspend fun deleteUserFromBoard(boardMemberId: String)

    suspend fun rollbackInvitation(invitedMemberId: String)

    suspend fun updateBoardName(boardInfo: BoardInfo): UpdateBoardNameResult

    suspend fun deleteBoard(boardId: String)
}