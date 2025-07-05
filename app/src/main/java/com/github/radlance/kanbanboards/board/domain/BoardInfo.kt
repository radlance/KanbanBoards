package com.github.radlance.kanbanboards.board.domain

data class BoardInfo(
    val id: String,
    val name: String,
    val isMyBoard: Boolean,
    val owner: String = ""
)