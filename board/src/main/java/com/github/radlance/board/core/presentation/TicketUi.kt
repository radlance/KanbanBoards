package com.github.radlance.board.core.presentation

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TicketUi(
    val colorHex: String,
    val id: String,
    val name: String,
    val description: String,
    val assignedMemberName: String,
    val assignedMemberId: String,
    val column: ColumnUi,
    val creationDate: LocalDateTime
)