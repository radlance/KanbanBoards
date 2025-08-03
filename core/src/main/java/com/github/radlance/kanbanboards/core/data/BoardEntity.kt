package com.github.radlance.kanbanboards.core.data

data class BoardEntity(
    val name: String = "",
    val owner: String = ""
)

data class BoardMemberEntity(
    val memberId: String = "",
    val boardId: String = ""
)