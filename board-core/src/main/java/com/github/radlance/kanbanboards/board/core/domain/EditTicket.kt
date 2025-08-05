package com.github.radlance.kanbanboards.board.core.domain

import java.time.LocalDateTime

data class EditTicket(
    val colorHex: String,
    val id: String,
    val boardId: String,
    val column: Column,
    val name: String,
    val description: String,
    val assignedMemberIds: List<String>,
    val creationDate: LocalDateTime
)
