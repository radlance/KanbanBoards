package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.Column
import javax.inject.Inject

class ColumnTypeMapper @Inject constructor(): Column.Mapper<String> {

    override fun mapTodo(): String = "todo"

    override fun mapInProgress(): String = "inProgress"

    override fun mapDone(): String = "done"
}