package com.github.radlance.kanbanboards.ticket.create.domain

data class NewTicket(
    val boardId: String,
    val colorHex: String,
    val name: String,
    val assignedMemberName: String
)