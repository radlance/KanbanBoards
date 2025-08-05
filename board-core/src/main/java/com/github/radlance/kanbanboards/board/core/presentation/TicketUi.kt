package com.github.radlance.kanbanboards.board.core.presentation

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TicketUi(
    val colorHex: String,
    val id: String,
    val name: String,
    val description: String,
    val assignedMemberNames: List<String>,
    val assignedMemberIds: List<String>,
    val column: ColumnUi,
    val creationDate: LocalDateTime
)