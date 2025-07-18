package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface TicketRemoteDataSource {

    fun tickets(boardId: String): Flow<List<Ticket>>

    fun moveTicket(ticketId: String, column: Column)

    suspend fun createTicket(newTicket: NewTicket)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError
    ) : TicketRemoteDataSource {

        @OptIn(ExperimentalCoroutinesApi::class)
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

                                val column = when (entity.columnId) {
                                    "todo" -> Column.Todo
                                    "inProgress" -> Column.InProgress
                                    "done" -> Column.Done
                                    else -> throw IllegalStateException("unknown column type")
                                }

                                with(entity) {
                                    Ticket(
                                        id = key,
                                        colorHex = color,
                                        name = title,
                                        description = description,
                                        assignedMemberName = userEntity?.name ?: "",
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
    }
}