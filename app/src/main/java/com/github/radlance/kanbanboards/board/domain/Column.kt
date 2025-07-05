package com.github.radlance.kanbanboards.board.domain

interface Column {

    val label: String

    object Todo : Column {

        override val label: String = "To Do"
    }

    object InProgress : Column {

        override val label: String = "In Progress"
    }

    object Done : Column {

        override val label: String = "Done"
    }
}