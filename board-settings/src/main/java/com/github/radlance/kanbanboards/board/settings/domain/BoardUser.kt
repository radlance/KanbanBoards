package com.github.radlance.kanbanboards.board.settings.domain

data class BoardUser(
    val id: String,
    val userId: String,
    val email: String,
    val name: String
)