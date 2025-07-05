package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun TicketColumns(
    tickets: List<Ticket>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            val columnModifier = Modifier
                .width(250.dp)

            TicketColumn(
                tickets = tickets.filter { it.column is Column.Todo },
                columnType = Column.Todo,
                modifier = columnModifier.padding(start = 12.dp)
            )

            TicketColumn(
                tickets = tickets.filter { it.column is Column.InProgress },
                columnType = Column.InProgress,
                modifier = columnModifier
            )

            TicketColumn(
                tickets = tickets.filter { it.column is Column.Done },
                columnType = Column.Done,
                modifier = columnModifier.padding(end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketRowsPreview() {
    KanbanBoardsTheme {
        TicketColumns(
            tickets = listOf(
                Ticket(
                    colorHex = "#BFE951",
                    id = "id",
                    name = "test another ticket",
                    assignedMemberName = "some member",
                    column = Column.Todo
                ),

                Ticket(
                    colorHex = "#EBC944",
                    id = "id2",
                    name = "test ticket",
                    assignedMemberName = "some member",
                    column = Column.InProgress
                ),

                Ticket(
                    colorHex = "#EBCAFF",
                    id = "id3",
                    name = "done ticket",
                    assignedMemberName = "some member",
                    column = Column.Done
                )
            ),
            modifier = Modifier.padding(vertical = 15.dp)
        )
    }
}