package com.github.radlance.kanbanboards.board.core.domain

import androidx.annotation.Keep

@Keep
data class BoardInfo(
    val id: String,
    val name: String,
    val isMyBoard: Boolean,
    val owner: String = ""
)