package com.github.radlance.kanbanboards.board.data

data class TicketEntity(
    val colorHex: String = "",
    val name: String = "",
    val assignedMemberName: String = "",
    val column: String = ""
)
