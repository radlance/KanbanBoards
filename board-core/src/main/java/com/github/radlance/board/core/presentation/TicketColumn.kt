package com.github.radlance.board.core.presentation

import android.content.ClipData
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.radlance.uikit.KanbanBoardsTheme
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

@Composable
internal fun TicketColumn(
    json: Json,
    tickets: List<TicketUi>,
    columnType: ColumnUi,
    onMove: (ticketId: String, column: ColumnUi) -> Unit,
    navigateToTicketInfo: (TicketUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
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
                    var show by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        show = true
                    }

                    val dragModifier = if (show) {
                        Modifier.dragAndDropSource { _ ->
                            DragAndDropTransferData(
                                ClipData.newPlainText("ticket", json.encodeToString(ticket))
                            )
                        }
                    } else Modifier

                    TicketItem(
                        ticket = ticket,
                        onMove = onMove,
                        navigateToTicketInfo = navigateToTicketInfo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .then(dragModifier)
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketColumnPreview() {
    KanbanBoardsTheme {
        TicketColumn(
            tickets = listOf(
                TicketUi(
                    colorHex = "#BFE951",
                    id = "id",
                    name = "test another ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    column = ColumnUi.Todo,
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    description = ""
                ),

                TicketUi(
                    colorHex = "#EBC944",
                    id = "id2",
                    name = "test ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    column = ColumnUi.Todo,
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    description = ""
                ),

                TicketUi(
                    colorHex = "#EBCAFF",
                    id = "id3",
                    name = "done ticket",
                    assignedMemberName = "some member",
                    assignedMemberId = "1",
                    column = ColumnUi.Todo,
                    creationDate = LocalDateTime(1, 1, 1, 1, 1),
                    description = ""
                )
            ),
            columnType = ColumnUi.Todo,
            onMove = { _, _ -> },
            json = Json,
            navigateToTicketInfo = {},
            modifier = Modifier
                .widthIn(250.dp)
                .padding(12.dp)
        )
    }
}