package com.github.radlance.kanbanboards.board.core.presentation

import android.content.ClipDescription
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
internal fun TicketColumnWithDrop(
    tickets: List<TicketUi>,
    columnType: ColumnUi,
    onMove: (ticketId: String, column: ColumnUi) -> Unit,
    navigateToTicketInfo: (TicketUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(ColumnUi::class) {
                subclass(ColumnUi.Todo::class, ColumnUi.Todo.serializer())
                subclass(ColumnUi.InProgress::class, ColumnUi.InProgress.serializer())
                subclass(ColumnUi.Done::class, ColumnUi.Done.serializer())
            }
        }
    }

    TicketColumn(
        tickets = tickets,
        columnType = columnType,
        onMove = onMove,
        json = json,
        navigateToTicketInfo = navigateToTicketInfo,
        modifier = modifier.dragAndDropTarget(
            shouldStartDragAndDrop = { event ->
                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
            },
            target = remember {
                object : DragAndDropTarget {
                    override fun onDrop(event: DragAndDropEvent): Boolean {
                        val dragData = event.toAndroidDragEvent()
                            .clipData
                            .getItemAt(0)
                            .text.toString()

                        val ticket = json.decodeFromString<TicketUi>(dragData)
                        onMove(ticket.id, columnType)
                        return true
                    }
                }
            }
        )
    )
}