package com.github.radlance.kanbanboards.board.domain

interface Column {

    object Todo : Column

    object InProgress : Column

    object Done : Column
}