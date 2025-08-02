package com.github.radlance.board.core.data

import com.github.radlance.board.core.domain.Column
import javax.inject.Inject

class ColumnTypeMapper @Inject constructor() : Column.Mapper<String> {

    override fun mapTodo(): String = "todo"

    override fun mapInProgress(): String = "inProgress"

    override fun mapDone(): String = "done"
}