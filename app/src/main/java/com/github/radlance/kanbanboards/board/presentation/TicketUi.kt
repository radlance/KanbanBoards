package com.github.radlance.kanbanboards.board.presentation

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TicketUi(
    val colorHex: String,
    val id: String,
    val name: String,
    val assignedMemberName: String,
    val column: ColumnUi,
    val creationDate: LocalDateTime
)