package com.github.radlance.board.core.presentation

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
import com.github.radlance.uikit.KanbanBoardsTheme
import kotlinx.datetime.LocalDateTime

@Composable
internal fun TicketBoard(
    tickets: List<TicketUi>,
    onMove: (ticketId: String, column: ColumnUi) -> Unit,
    navigateToTicketInfo: (TicketUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            val columnModifier = Modifier
                .width(250.dp)

            val sortedTickets = tickets.sortedBy { it.creationDate }

            TicketColumnWithDrop(
                tickets = sortedTickets.filter { it.column is ColumnUi.Todo },
                columnType = ColumnUi.Todo,
                onMove = onMove,
                navigateToTicketInfo = navigateToTicketInfo,
                modifier = columnModifier.padding(start = 12.dp)
            )

            TicketColumnWithDrop(
                tickets = sortedTickets.filter { it.column is ColumnUi.InProgress },
                columnType = ColumnUi.InProgress,
                onMove = onMove,
                navigateToTicketInfo = navigateToTicketInfo,
                modifier = columnModifier
            )

            TicketColumnWithDrop(
                tickets = sortedTickets.filter { it.column is ColumnUi.Done },
                columnType = ColumnUi.Done,
                onMove = onMove,
                navigateToTicketInfo = navigateToTicketInfo,
                modifier = columnModifier.padding(end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketBoardPreview() {
    KanbanBoardsTheme {
        TicketBoard(
            tickets = listOf(
                TicketUi(
                    colorHex = "#BFE951",
                    id = "id",
                    name = "test another ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    column = ColumnUi.Todo,
                    description = ""
                ),

                TicketUi(
                    colorHex = "#EBC944",
                    id = "id2",
                    name = "test ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    column = ColumnUi.InProgress,
                    description = ""
                ),

                TicketUi(
                    colorHex = "#EBCAFF",
                    id = "id3",
                    name = "done ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    column = ColumnUi.Done,
                    description = ""
                )
            ),
            onMove = { _, _ -> },
            navigateToTicketInfo = {},
            modifier = Modifier.padding(vertical = 15.dp)
        )
    }
}