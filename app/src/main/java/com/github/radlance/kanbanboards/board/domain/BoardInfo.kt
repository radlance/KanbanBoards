package com.github.radlance.kanbanboards.board.domain

import com.github.radlance.kanbanboards.boards.domain.Board
import javax.inject.Inject

data class BoardInfo(
    val id: String,
    val name: String,
    val isMyBoard: Boolean,
    val owner: String = ""
)

class BoardStorageMapper @Inject constructor(): Board.StorageMapper<BoardInfo> {

    override fun mapMyBoard(id: String, name: String): BoardInfo = BoardInfo(
        id = id, name = name, isMyBoard = true
    )

    override fun mapOtherBoard(id: String, name: String, owner: String): BoardInfo = BoardInfo(
        id = id, name = name, isMyBoard = false, owner = owner
    )
}