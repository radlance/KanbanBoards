package com.github.radlance.kanbanboards.board.core.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import javax.inject.Inject

class ColumnMapper @Inject constructor() : Column.Mapper<ColumnUi> {

    override fun mapTodo(): ColumnUi = ColumnUi.Todo

    override fun mapInProgress(): ColumnUi = ColumnUi.InProgress

    override fun mapDone(): ColumnUi = ColumnUi.Done
}