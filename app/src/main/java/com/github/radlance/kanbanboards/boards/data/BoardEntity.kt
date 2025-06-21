package com.github.radlance.kanbanboards.boards.data

data class BoardEntity(
    val name: String = "",
    val owner: String = ""
)

data class OtherBoardEntity(
    val memberId: String = "",
    val boardId: String  = ""
)