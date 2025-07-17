package com.github.radlance.kanbanboards.board.data

data class TicketEntity(
    val boardId: String = "",
    val color: String = "",
    val title: String = "",
    val assignee: String = "",
    val columnId: String = ""
)
