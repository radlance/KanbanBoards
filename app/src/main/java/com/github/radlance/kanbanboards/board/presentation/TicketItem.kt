package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun TicketItem(
    ticket: TicketUi,
    onMove: (ticketId: String, column: ColumnUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondary
            ).background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = ticket.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = ticket.assignedMemberName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    ticket.column.StartContent { onMove(ticket.id, it) }
                }

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 55.dp)
                        .background(color = Color(ticket.colorHex.toColorInt()))
                )

                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    ticket.column.EndContent { onMove(ticket.id, it) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketItemPreview() {
    KanbanBoardsTheme {
        TicketItem(
            ticket = TicketUi(
                colorHex = "#EBC944",
                id = "id",
                name = "test ticket",
                assignedMemberName = "some member",
                column = ColumnUi.Todo
            ),
            onMove = { _, _ -> },
            modifier = Modifier
                .width(250.dp)
                .padding(12.dp)
        )
    }
}