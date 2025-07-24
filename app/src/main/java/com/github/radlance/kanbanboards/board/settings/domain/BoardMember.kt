package com.github.radlance.kanbanboards.board.settings.domain

data class BoardMember(
    val boardMemberId: String,
    val userId: String,
    val email: String,
    val name: String
)