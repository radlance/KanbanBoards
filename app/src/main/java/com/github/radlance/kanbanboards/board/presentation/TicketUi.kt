package com.github.radlance.kanbanboards.board.presentation

data class TicketUi(
    val colorHex: String,
    val id: String,
    val name: String,
    val assignedMemberName: String,
    val column: ColumnUi
)