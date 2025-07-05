package com.github.radlance.kanbanboards.board.domain

data class Ticket(
    val colorHex: String,
    val id: String,
    val name: String,
    val assignedMemberName: String,
    val column: Column
)