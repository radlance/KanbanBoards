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
                        val column = when (column) {
                            "todo" -> Column.Todo
                            "in progress" -> Column.InProgress
                            else -> Column.Done
                        }

                        Ticket(
                            id = key,
                            colorHex = colorHex,
                            name = name,
                            assignedMemberName = assignedMemberName,
                            column = column
                        )
                    }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }
    }
}