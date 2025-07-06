package com.github.radlance.kanbanboards.board.domain

data class Ticket(
    val id: String,
    val colorHex: String,
    val name: String,
    val assignedMemberName: String,
    val column: Column
)