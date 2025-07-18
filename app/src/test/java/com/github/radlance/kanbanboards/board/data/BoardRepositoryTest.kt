package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.board.domain.TicketResult
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BoardRepositoryTest : BaseTest() {

    private lateinit var boardRemoteDataSource: TestBoardRemoteDataSource
    private lateinit var ticketRemoteDataSource: TestTicketRemoteDataSource

    private lateinit var repository: RemoteBoardRepository

    @Before
    fun setup() {
        boardRemoteDataSource = TestBoardRemoteDataSource()
        ticketRemoteDataSource = TestTicketRemoteDataSource()

        repository = RemoteBoardRepository(
            boardRemoteDataSource = boardRemoteDataSource,
            ticketRemoteDataSource = ticketRemoteDataSource
        )
    }

    @Test
    fun test_board_success() = runBlocking {
        val board = repository.board(boardId = "test board id")
        assertEquals(
            BoardResult.Success(
                boardInfo = BoardInfo(
                    id = "test board id",
                    name = "default name",
                    isMyBoard = true,
                    owner = "default owner"
                )
            ),
            board.first()
        )
        assertEquals(1, boardRemoteDataSource.loadBoardCalledList.size)
        assertEquals("test board id", boardRemoteDataSource.loadBoardCalledList[0])
    }

    @Test
    fun test_board_error() = runBlocking {
        boardRemoteDataSource.makeExpectedBoardException(IllegalStateException("server error"))
        val actual = repository.board(boardId = "stub").toList()

        assertEquals(emptyList<BoardResult>(), actual)
    }

    @Test
    fun test_tickets_success() = runBlocking {
        ticketRemoteDataSource.makeExpectedTickets(
            tickets = listOf(
                Ticket(
                    id = "ticketId",
                    colorHex = "#EAAEEE",
                    name = "ticketName",
                    assignedMemberName = "member",
                    description = "ticketDescription",
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 7, 18, 6, 30)
                )
            )
        )

        assertEquals(
            TicketResult.Success(
                listOf(
                    Ticket(
                        id = "test board id",
                        colorHex = "#EAAEEE",
                        name = "ticketName",
                        assignedMemberName = "member",
                        description = "ticketDescription",
                        column = Column.Todo,
                        creationDate = LocalDateTime.of(2025, 7, 18, 6, 30)
                    )
                )
            ),
            repository.tickets(boardId = "test board id").first()
        )
        assertEquals(1, ticketRemoteDataSource.ticketsCalledList.size)
        assertEquals("test board id", ticketRemoteDataSource.ticketsCalledList[0])
    }

    @Test
    fun test_tickets_error() = runBlocking {
        ticketRemoteDataSource.makeExpectedTicketsException(IllegalStateException("server error"))
        val actual = repository.tickets(boardId = "stub").toList()

        assertEquals(emptyList<TicketResult>(), actual)
    }

    @Test
    fun test_move_ticket() = runBlocking {
        repository.moveTicket(ticketId = "move ticket id", column = Column.Done)
        assertEquals(1, ticketRemoteDataSource.moveTicketCalledList.size)
        assertEquals(
            Pair("move ticket id", Column.Done),
            ticketRemoteDataSource.moveTicketCalledList[0]
        )
    }

    private class TestBoardRemoteDataSource : BoardRemoteDataSource {

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

        fun makeExpectedBoardException(exception: Exception) {
            boardException = exception
        }

        private var boardMembersException: Exception? = null
        val boardMembersCalledList = mutableListOf<String>()
        private val boardMembers = MutableStateFlow<List<BoardMember>>(emptyList())

        fun makeExpectedBoardMembersException(exception: Exception) {
            boardException = exception
        }

        override fun loadBoard(boardId: String): Flow<BoardInfo> = flow {
            loadBoardCalledList.add(boardId)
            boardException?.let { throw it }
            boardInfo.update { it.copy(id = boardId) }
            emitAll(boardInfo)
        }

        override fun boardMembers(boardId: String): Flow<List<BoardMember>> = flow {
            boardMembersCalledList.add(boardId)
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }
    }

    private class TestTicketRemoteDataSource : TicketRemoteDataSource {

        private var ticketsException: Exception? = null
        val ticketsCalledList = mutableListOf<String>()
        private val tickets = MutableStateFlow(emptyList<Ticket>())
        val moveTicketCalledList = mutableListOf<Pair<String, Column>>()

        private var createTicketException: Exception? = null
        val createTicketCalledList = mutableListOf<NewTicket>()

        fun makeExpectedTickets(tickets: List<Ticket>) {
            this.tickets.value = tickets
        }

        fun makeExpectedCreateTicketException(exception: Exception) {
            ticketsException = exception
        }

        fun makeExpectedTicketsException(exception: Exception) {
            ticketsException = exception
        }

        override fun tickets(boardId: String): Flow<List<Ticket>> = flow {
            ticketsCalledList.add(boardId)
            ticketsException?.let { throw it }
            emitAll(tickets.map { list -> list.map { it.copy(id = boardId) } })
        }

        override fun moveTicket(ticketId: String, column: Column) {
            moveTicketCalledList.add(Pair(ticketId, column))
        }

        override suspend fun createTicket(newTicket: NewTicket) {
            createTicketCalledList.add(newTicket)
            createTicketException?.let { throw it }
        }
    }
}