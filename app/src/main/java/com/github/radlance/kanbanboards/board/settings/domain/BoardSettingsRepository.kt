package com.github.radlance.kanbanboards.board.settings.domain

import kotlinx.coroutines.flow.Flow

interface BoardSettingsRepository {

    fun boardSettings(boardId: String, ownerId: String): Flow<BoardSettingsResult>
}