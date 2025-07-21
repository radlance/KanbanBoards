package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme
import kotlinx.datetime.LocalDateTime

@Composable
fun TicketItem(
    ticket: TicketUi,
    onMove: (ticketId: String, column: ColumnUi) -> Unit,
    navigateToTicketInfo: (TicketUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        val contentPaddingModifier = 12.dp
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = contentPaddingModifier)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPaddingModifier),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = stringResource(R.string.more),
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        .padding(5.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple()
                        ) { navigateToTicketInfo(ticket) }
                )
            }
            Text(
                text = ticket.assignedMemberName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = contentPaddingModifier)
            )
            Spacer(Modifier.height(3.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPaddingModifier),
                contentAlignment = Alignment.Center
            ) {

                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    ticket.column.StartContent { onMove(ticket.id, it) }
                }

                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    ticket.column.EndContent { onMove(ticket.id, it) }
                }
            }
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(color = Color(ticket.colorHex.toColorInt()))
            )
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
                column = ColumnUi.Todo,
                description = "",
                creationDate = LocalDateTime(1, 1, 1, 1, 1)
            ),
            onMove = { _, _ -> },
            navigateToTicketInfo = {},
            modifier = Modifier
                .width(250.dp)
                .padding(12.dp)
        )
    }
}