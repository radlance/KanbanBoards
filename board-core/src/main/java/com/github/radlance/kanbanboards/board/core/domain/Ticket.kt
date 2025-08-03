package com.github.radlance.kanbanboards.board.core.domain

import java.time.LocalDateTime

data class Ticket(
    val id: String,
    val colorHex: String,
    val name: String,
    val description: String,
    val assignedMemberName: String,
    val assignedMemberId: String,
    val column: Column,
    val creationDate: LocalDateTime
)