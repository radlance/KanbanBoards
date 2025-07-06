package com.github.radlance.kanbanboards.board.data

data class BoardEntity(
    val name: String = "",
    val owner: String = ""
)

data class OtherBoardEntity(
    val memberId: String = "",
    val boardId: String  = ""
)