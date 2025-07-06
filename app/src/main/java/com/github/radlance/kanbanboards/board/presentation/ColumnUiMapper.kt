package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.Column
import javax.inject.Inject

class ColumnUiMapper @Inject constructor(): ColumnUi.Mapper<Column> {

    override fun mapTodo(): Column = Column.Todo

    override fun mapInProgress(): Column = Column.InProgress

    override fun mapDone(): Column = Column.Done
}