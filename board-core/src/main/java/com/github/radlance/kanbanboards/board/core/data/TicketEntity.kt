package com.github.radlance.kanbanboards.board.core.data

internal data class TicketEntity(
    val boardId: String = "",
    val color: String = "",
    val title: String = "",
    val description: String = "",
    val assignee: List<String> = emptyList(),
    val columnId: String = "",
    val creationDate: String = ""
)
