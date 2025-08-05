package com.github.radlance.kanbanboards.board.core.domain

import java.time.LocalDateTime

data class NewTicket(
    val boardId: String,
    val colorHex: String,
    val name: String,
    val description: String,
    val assignedMemberIds: List<String>,
    val creationDate: LocalDateTime
)