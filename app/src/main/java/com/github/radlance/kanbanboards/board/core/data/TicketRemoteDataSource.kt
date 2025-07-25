package com.github.radlance.kanbanboards.board.core.data

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface TicketRemoteDataSource {

    fun ticket(ticketId: String): Flow<Ticket?>

    fun tickets(boardId: String): Flow<List<Ticket>>

    fun moveTicket(ticketId: String, column: Column)

    suspend fun createTicket(newTicket: NewTicket)

    suspend fun editTicket(ticket: EditTicket)

    suspend fun deleteTicket(ticketId: String)

    @OptIn(ExperimentalCoroutinesApi::class)
    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError,
        private val columnMapper: ColumnTypeMapper
    ) : TicketRemoteDataSource {

        override fun ticket(ticketId: String): Flow<Ticket?> {
            val ticketQuery = provideDatabase.database()
                .child("tickets")
                .child(ticketId)
            return ticketQuery.snapshots.flatMapLatest { ticketSnapshot ->
                val entity =
                    ticketSnapshot.getValue<TicketEntity>()

                if (entity == null) {
                    flowOf(null)
                } else {
                    provideDatabase.database()
                        .child("users")
                        .child(entity.assignee)
                        .snapshots.mapNotNull { userSnapshot ->
                            val userEntity = userSnapshot.getValue<UserProfileEntity>()

                            val column = columnType(entity)

                            with(entity) {
                                Ticket(
                                    id = ticketSnapshot.key ?: return@mapNotNull null,
                                    colorHex = color,
                                    name = title,
                                    description = description,
                                    assignedMemberName = userEntity?.name ?: "",
                                    assignedMemberId = entity.assignee,
                                    column = column,
                                    creationDate = LocalDateTime.parse(creationDate)
                                )
                            }
                        }
                }

            }
        }

        override fun tickets(boardId: String): Flow<List<Ticket>> {
            val ticketsQuery = provideDatabase.database()
                .child("tickets")
                .orderByChild("boardId")
                .equalTo(boardId)

            return ticketsQuery.snapshots.flatMapLatest { snapshot ->
                val ticketFlows: List<Flow<Ticket>> =
                    snapshot.children.mapNotNull { ticketSnapshot ->
                        val key = ticketSnapshot.key ?: return@mapNotNull null
                        val entity =
                            ticketSnapshot.getValue<TicketEntity>() ?: return@mapNotNull null

                        provideDatabase.database()
                            .child("users")
                            .child(entity.assignee)
                            .snapshots
                            .map { userSnapshot ->
                                val userEntity = userSnapshot.getValue<UserProfileEntity>()

                                val column = columnType(entity)

                                with(entity) {
                                    Ticket(
                                        id = key,
                                        colorHex = color,
                                        name = title,
                                        description = description,
                                        assignedMemberName = userEntity?.name ?: "",
                                        assignedMemberId = entity.assignee,
                                        column = column,
                                        creationDate = LocalDateTime.parse(creationDate)
                                    )
                                }
                            }
                    }

                if (ticketFlows.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(ticketFlows) { ticketsArray: Array<Ticket> -> ticketsArray.toList() }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override fun moveTicket(ticketId: String, column: Column) {

            provideDatabase.database()
                .child("tickets")
                .child(ticketId)
                .child("columnId")
                .setValue(column.map(columnMapper))
        }

        override suspend fun createTicket(newTicket: NewTicket) {
            try {
                val reference = provideDatabase.database().child("tickets").push()
                with(newTicket) {
                    reference.setValue(
                        TicketEntity(
                            boardId = boardId,
                            color = colorHex,
                            title = name,
                            description = description,
                            assignee = assignedMemberId,
                            columnId = "todo",
                            creationDate = creationDate.toString()
                        )
                    ).await()
                }
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        override suspend fun editTicket(ticket: EditTicket) {
            try {
                val ticketReference = provideDatabase.database()
                    .child("tickets")
                    .child(ticket.id)

                ticketReference.setValue(
                    with(ticket) {
                        TicketEntity(
                            boardId = boardId,
                            color = colorHex,
                            title = name,
                            description = description,
                            assignee = assignedMemberId,
                            columnId = column.map(columnMapper),
                            creationDate = creationDate.toString()
                        )
                    }
                ).await()

            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        override suspend fun deleteTicket(ticketId: String) {
            try {
                provideDatabase.database()
                    .child("tickets")
                    .child(ticketId)
                    .removeValue()
                    .await()
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        private fun columnType(entity: TicketEntity) = when (entity.columnId) {
            "todo" -> Column.Todo
            "inProgress" -> Column.InProgress
            "done" -> Column.Done
            else -> throw IllegalStateException("unknown column type")
        }
    }
}