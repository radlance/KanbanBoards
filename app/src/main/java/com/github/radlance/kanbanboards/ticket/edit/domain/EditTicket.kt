package com.github.radlance.kanbanboards.ticket.edit.domain

import com.github.radlance.kanbanboards.board.domain.Column
import java.time.LocalDateTime

data class EditTicket(
    val colorHex: String,
    val id: String,
    val boardId: String,
    val column: Column,
    val name: String,
    val description: String,
    val assignedMemberId: String,
    val creationDate: LocalDateTime
)
