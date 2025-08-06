package com.github.radlance.kanbanboards.board.core.data

import com.github.radlance.kanbanboards.api.service.Service
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.EditTicket
import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.core.data.HandleError
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.time.LocalDateTime
import javax.inject.Inject

interface TicketRemoteDataSource {

    fun ticket(ticketId: String): Flow<Ticket?>

    fun tickets(boardId: String): Flow<List<Ticket>>

    fun moveTicket(ticketId: String, column: Column)

    fun createTicket(newTicket: NewTicket)

    fun editTicket(ticket: EditTicket)

    fun deleteTicket(ticketId: String)
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class BaseTicketRemoteDataSource @Inject constructor(
    private val service: Service,
    private val handleError: HandleError,
    private val columnMapper: Column.Mapper<String>
) : TicketRemoteDataSource {

    override fun ticket(ticketId: String): Flow<Ticket?> {
        val tickets = service.get(path = "tickets", subPath = ticketId)
        return tickets.flatMapLatest { ticketSnapshot ->
            val entity = ticketSnapshot.getValue(TicketEntity::class.java)

            if (entity == null) {
                flowOf(null)
            } else {
                val userFlows = entity.assignee.map { assigneeId ->
                    service.get(path = "users", subPath = assigneeId)
                        .mapNotNull { userSnapshot ->
                            userSnapshot.getValue(UserProfileEntity::class.java)?.name
                        }
                }

                if (userFlows.isEmpty()) {
                    flowOf(
                        with(entity) {
                            Ticket(
                                id = ticketSnapshot.id,
                                colorHex = color,
                                name = title,
                                description = description,
                                assignedMemberNames = emptyList(),
                                assignedMemberIds = entity.assignee,
                                column = columnType(entity),
                                creationDate = LocalDateTime.parse(creationDate)
                            )
                        }
                    )
                } else {
                    combine(userFlows) { namesArray ->
                        with(entity) {
                            Ticket(
                                id = ticketSnapshot.id,
                                colorHex = color,
                                name = title,
                                description = description,
                                assignedMemberNames = namesArray.toList(),
                                assignedMemberIds = entity.assignee,
                                column = columnType(entity),
                                creationDate = LocalDateTime.parse(creationDate)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun tickets(boardId: String): Flow<List<Ticket>> {
        val ticketsQuery = service.getListByQuery(
            path = "tickets",
            queryKey = "boardId",
            queryValue = boardId
        )

        return ticketsQuery.flatMapLatest { snapshot ->
            val ticketFlows: List<Flow<Ticket>> = snapshot.mapNotNull { ticketSnapshot ->
                val key = ticketSnapshot.id
                val entity = ticketSnapshot.getValue(TicketEntity::class.java)
                    ?: return@mapNotNull null

                if (entity.assignee.isEmpty()) {
                    flowOf(
                        with(entity) {
                            Ticket(
                                id = key,
                                colorHex = color,
                                name = title,
                                description = description,
                                assignedMemberNames = emptyList(),
                                assignedMemberIds = emptyList(),
                                column = columnType(entity),
                                creationDate = LocalDateTime.parse(creationDate)
                            )
                        }
                    )
                } else {
                    val userFlows = entity.assignee.map { assigneeId ->
                        service.get(path = "users", subPath = assigneeId)
                            .map { userSnapshot ->
                                userSnapshot.getValue(UserProfileEntity::class.java)?.name ?: ""
                            }
                    }

                    combine(userFlows) { namesArray ->
                        with(entity) {
                            Ticket(
                                id = key,
                                colorHex = color,
                                name = title,
                                description = description,
                                assignedMemberNames = namesArray.toList(),
                                assignedMemberIds = entity.assignee,
                                column = columnType(entity),
                                creationDate = LocalDateTime.parse(creationDate)
                            )
                        }
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

        service.update(
            path = "tickets",
            subPath1 = ticketId,
            subPath2 = "columnId",
            obj = column.map(columnMapper)
        )
    }

    override fun createTicket(newTicket: NewTicket) {
        try {
            val entity = with(newTicket) {
                TicketEntity(
                    boardId = boardId,
                    color = colorHex,
                    title = name,
                    description = description,
                    assignee = assignedMemberIds,
                    columnId = "todo",
                    creationDate = creationDate.toString()
                )
            }

            service.post(
                path = "tickets",
                obj = entity
            )

        } catch (e: Exception) {
            handleError.handle(e)
        }
    }

    override fun editTicket(ticket: EditTicket) {
        try {
            val entity = with(ticket) {
                TicketEntity(
                    boardId = boardId,
                    color = colorHex,
                    title = name,
                    description = description,
                    assignee = assignedMemberIds,
                    columnId = column.map(columnMapper),
                    creationDate = creationDate.toString()
                )
            }

            service.update(
                path = "tickets",
                subPath = ticket.id,
                obj = entity
            )

        } catch (e: Exception) {
            handleError.handle(e)
        }
    }

    override fun deleteTicket(ticketId: String) {
        try {
            service.delete(
                path = "tickets",
                itemId = ticketId
            )
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