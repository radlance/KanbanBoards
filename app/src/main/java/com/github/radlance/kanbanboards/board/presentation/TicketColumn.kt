package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun TicketColumn(
    tickets: List<Ticket>,
    columnType: Column,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.border(
            width = 1.dp,
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
        ) {
            Text(text = columnType.label, fontSize = 30.sp)
            Spacer(Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = tickets, key = { it.id }) { ticket ->
                    TicketItem(ticket, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketColumnPreview() {
    KanbanBoardsTheme {
        TicketColumn(
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
                    column = Column.Todo
                ),

                Ticket(
                    colorHex = "#EBCAFF",
                    id = "id3",
                    name = "done ticket",
                    assignedMemberName = "some member",
                    column = Column.Todo
                )
            ),
            columnType = Column.Todo,
            modifier = Modifier
                .widthIn(250.dp)
                .padding(12.dp)
        )
    }
}