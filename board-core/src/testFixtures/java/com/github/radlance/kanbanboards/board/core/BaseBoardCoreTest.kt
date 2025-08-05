package com.github.radlance.kanbanboards.board.core

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.EditTicket
import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.domain.BoardInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

abstract class BaseBoardCoreTest : BaseTest() {

    protected class TestBoardRemoteDataSource : BoardRemoteDataSource {

        private var boardException: Exception? = null
        val loadBoardCalledList = mutableListOf<String>()
        private val boardInfo = MutableStateFlow(
            BoardInfo(
                id = "default id",
                name = "default name",
                isMyBoard = true,
                owner = "default owner"
            )
        )

        val leaveBoardCalledList = mutableListOf<String>()

        val deleteBoardCalledList = mutableListOf<String>()

        fun makeExpectedBoardException(exception: Exception) {
            boardException = exception
        }

        override fun board(boardId: String): Flow<BoardInfo> = flow {
            loadBoardCalledList.add(boardId)
            boardException?.let { throw it }
            boardInfo.update { it.copy(id = boardId) }
            emitAll(boardInfo)
        }

        override suspend fun leaveBoard(boardId: String) {
            leaveBoardCalledList.add(boardId)
        }

        override suspend fun deleteBoard(boardId: String) {
            deleteBoardCalledList.add(boardId)
        }
    }

    protected class TestTicketRemoteDataSource : TicketRemoteDataSource {

        private var ticketsException: Exception? = null
        val ticketsCalledList = mutableListOf<String>()
        private val tickets = MutableStateFlow(emptyList<Ticket>())
        val moveTicketCalledList = mutableListOf<Pair<String, Column>>()

        private var createTicketException: Exception? = null
        val createTicketCalledList = mutableListOf<NewTicket>()

        private val ticket = MutableStateFlow<Ticket?>(
            Ticket(
                id = "initial id",
                colorHex = "initial color",
                name = "initial name",
                description = "initial description",
                assignedMemberNames = listOf("initial assignee"),
                column = Column.Todo,
                creationDate = LocalDateTime.of(2025, 4, 4, 4, 4),
                assignedMemberIds = listOf("initial assigned member id")
            )
        )
        private var ticketException: Exception? = null
        val ticketCalledList = mutableListOf<String>()

        private var editTicketException: Exception? = null
        val editTicketCalledList = mutableListOf<EditTicket>()

        private var deleteTicketException: Exception? = null
        val deleteTicketCalledList = mutableListOf<String>()

        fun makeExpectedTickets(tickets: List<Ticket>) {
            this.tickets.value = tickets
        }

        fun makeExpectedTicket(ticket: Ticket?) {
            this.ticket.value = ticket
        }

        fun makeExpectedTicketsException(exception: Exception) {
            ticketsException = exception
        }

        fun makeExpectedCreateTicketException(exception: Exception) {
            createTicketException = exception
        }

        fun makeExpectedTicketException(exception: Exception) {
            ticketException = exception
        }

        fun makeExpectedEditTicketException(exception: Exception) {
            editTicketException = exception
        }

        fun makeExpectedDeleteTicketException(exception: Exception) {
            deleteTicketException = exception
        }

        override fun ticket(ticketId: String): Flow<Ticket?> = flow {
            ticketCalledList.add(ticketId)
            ticketException?.let { throw it }
            emitAll(
                if (ticket.value == null) {
                    flowOf(null)
                } else {
                    ticket.map { it!!.copy(id = ticketId) }
                }
            )
        }

        override fun tickets(boardId: String): Flow<List<Ticket>> = flow {
            ticketsCalledList.add(boardId)
            ticketsException?.let { throw it }
            emitAll(tickets.map { list -> list.map { it.copy(id = boardId) } })
        }

        override fun moveTicket(ticketId: String, column: Column) {
            moveTicketCalledList.add(Pair(ticketId, column))
        }

        override fun createTicket(newTicket: NewTicket) {
            createTicketCalledList.add(newTicket)
            createTicketException?.let { throw it }
        }

        override fun editTicket(ticket: EditTicket) {
            editTicketCalledList.add(ticket)
            editTicketException?.let { throw it }
        }

        override fun deleteTicket(ticketId: String) {
            deleteTicketCalledList.add(ticketId)
            deleteTicketException?.let { throw it }
        }
    }

    protected class TestBoardRepository : BoardRepository {
        val boardCalledList = mutableListOf<String>()

        private val boardResult: MutableStateFlow<BoardResult> = MutableStateFlow(
            BoardResult.Error(message = "default board error message")
        )

        fun makeExpectedBoardResult(boardResult: BoardResult) {
            this.boardResult.value = boardResult
        }

        val ticketsCalledList = mutableListOf<String>()

        private val ticketResult: MutableStateFlow<TicketResult> = MutableStateFlow(
            TicketResult.Error(message = "default ticket error message")
        )

        fun makeExpectedTicketResult(ticketResult: TicketResult) {
            this.ticketResult.value = ticketResult
        }

        val moveTicketCalledList = mutableListOf<Pair<String, Column>>()

        val leaveBoardCalledList = mutableListOf<String>()

        val deleteBoardCalledList = mutableListOf<String>()

        override fun board(boardId: String): Flow<BoardResult> {
            boardCalledList.add(boardId)
            return boardResult
        }

        override fun tickets(boardId: String): Flow<TicketResult> {
            ticketsCalledList.add(boardId)
            return ticketResult
        }

        override fun moveTicket(ticketId: String, column: Column) {
            moveTicketCalledList.add(Pair(ticketId, column))
        }

        override suspend fun leaveBoard(boardId: String) {
            leaveBoardCalledList.add(boardId)
        }

        override suspend fun deleteBoard(boardId: String) {
            deleteBoardCalledList.add(boardId)
        }
    }
}