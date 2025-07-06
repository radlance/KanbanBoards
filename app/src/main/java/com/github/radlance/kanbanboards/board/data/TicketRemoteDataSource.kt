package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TicketRemoteDataSource {

    fun tickets(boardId: String): Flow<List<Ticket>>

    fun moveTicket(ticketId: String, column: Column)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : TicketRemoteDataSource {

        override fun tickets(boardId: String): Flow<List<Ticket>> {
            val ticketsQuery = provideDatabase.database()
                .child("tickets")
                .orderByChild("boardId")
                .equalTo(boardId)

            return ticketsQuery.snapshots.map { snapshot ->
                snapshot.children.mapNotNull {
                    val key = it.key ?: return@mapNotNull null
                    val entity = it.getValue<TicketEntity>() ?: return@mapNotNull null

                    with(entity) {
                        val column = when (columnId) {
                            "todo" -> Column.Todo
                            "inProgress" -> Column.InProgress
                            "done" -> Column.Done
                            else -> throw IllegalStateException("unknown column type")
                        }

                        Ticket(
                            id = key,
                            colorHex = color,
                            name = title,
                            assignedMemberName = assignee,
                            column = column
                        )
                    }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override fun moveTicket(ticketId: String, column: Column) {

            val columnLabel = when (column) {
                is Column.Todo -> "todo"
                is Column.InProgress -> "inProgress"
                is Column.Done -> "done"
                else -> throw IllegalStateException("unknown column type")
            }

            provideDatabase.database()
                .child("tickets")
                .child(ticketId)
                .child("columnId")
                .setValue(columnLabel)
        }
    }
}