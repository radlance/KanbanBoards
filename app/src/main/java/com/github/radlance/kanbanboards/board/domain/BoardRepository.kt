package com.github.radlance.kanbanboards.board.domain

import kotlinx.coroutines.flow.Flow

interface BoardRepository {

    fun loadBoard(boardId: String): Flow<BoardResult>
}