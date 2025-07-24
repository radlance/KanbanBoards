package com.github.radlance.kanbanboards.board.settings.domain

import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import kotlinx.coroutines.flow.Flow

interface BoardSettingsRepository {

    fun board(boardId: String): Flow<BoardResult>

    fun boardSettings(boardId: String, ownerId: String): Flow<BoardSettingsResult>
}