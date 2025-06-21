package com.github.radlance.kanbanboards.common.data

data class BoardInfo(
    val id: String,
    val name: String,
    val isMyBoard: Boolean,
    val ownerId: String = ""
)
