package com.github.radlance.kanbanboards.ticket.create.domain

import java.time.LocalDateTime

data class NewTicket(
    val boardId: String,
    val colorHex: String,
    val name: String,
    val description: String,
    val assignedMemberName: String,
    val creationDate: LocalDateTime
)