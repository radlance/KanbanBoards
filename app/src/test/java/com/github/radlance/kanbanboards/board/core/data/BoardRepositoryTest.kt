//package com.github.radlance.kanbanboards.board.core.data
//
//import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
//import com.github.radlance.kanbanboards.board.core.domain.BoardResult
//import com.github.radlance.kanbanboards.board.core.domain.Column
//import com.github.radlance.kanbanboards.board.core.domain.Ticket
//import com.github.radlance.kanbanboards.board.core.domain.TicketResult
//import com.github.radlance.kanbanboards.common.BaseTest
//import com.github.radlance.kanbanboards.common.data.IgnoreHandle
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Test
//import java.time.LocalDateTime
//
//class BoardRepositoryTest : BaseTest() {
//
//    private lateinit var boardRemoteDataSource: TestBoardRemoteDataSource
//    private lateinit var ticketRemoteDataSource: TestTicketRemoteDataSource
//
//    private lateinit var repository: RemoteBoardRepository
//
//    @Before
//    fun setup() {
//        boardRemoteDataSource = TestBoardRemoteDataSource()
//        ticketRemoteDataSource = TestTicketRemoteDataSource()
//
//        repository = RemoteBoardRepository(
//            boardRemoteDataSource = boardRemoteDataSource,
//            ticketRemoteDataSource = ticketRemoteDataSource,
//            ignoreHandle = IgnoreHandle.Base()
//        )
//    }
//
//    @Test
//    fun test_board_success() = runBlocking {
//        val board = repository.board(boardId = "test board id")
//        assertEquals(
//            BoardResult.Success(
//                boardInfo = BoardInfo(
//                    id = "test board id",
//                    name = "default name",
//                    isMyBoard = true,
//                    owner = "default owner"
//                )
//            ),
//            board.first()
//        )
//        assertEquals(1, boardRemoteDataSource.loadBoardCalledList.size)
//        assertEquals("test board id", boardRemoteDataSource.loadBoardCalledList[0])
//    }
//
//    @Test
//    fun test_board_error() = runBlocking {
//        boardRemoteDataSource.makeExpectedBoardException(IllegalStateException("server error"))
//        val actual = repository.board(boardId = "stub").toList()
//
//        assertEquals(emptyList<BoardResult>(), actual)
//    }
//
//    @Test
//    fun test_tickets_success() = runBlocking {
//        ticketRemoteDataSource.makeExpectedTickets(
//            tickets = listOf(
//                Ticket(
//                    id = "ticketId",
//                    colorHex = "#EAAEEE",
//                    name = "ticketName",
//                    assignedMemberName = "member",
//                    description = "ticketDescription",
//                    column = Column.Todo,
//                    creationDate = LocalDateTime.of(2025, 7, 18, 6, 30),
//                    assignedMemberId = "assignedMemberId"
//                )
//            )
//        )
//
//        assertEquals(
//            TicketResult.Success(
//                listOf(
//                    Ticket(
//                        id = "test board id",
//                        colorHex = "#EAAEEE",
//                        name = "ticketName",
//                        assignedMemberName = "member",
//                        description = "ticketDescription",
//                        column = Column.Todo,
//                        creationDate = LocalDateTime.of(2025, 7, 18, 6, 30),
//                        assignedMemberId = "assignedMemberId"
//                    )
//                )
//            ),
//            repository.tickets(boardId = "test board id").first()
//        )
//        assertEquals(1, ticketRemoteDataSource.ticketsCalledList.size)
//        assertEquals("test board id", ticketRemoteDataSource.ticketsCalledList[0])
//    }
//
//    @Test
//    fun test_tickets_error() = runBlocking {
//        ticketRemoteDataSource.makeExpectedTicketsException(IllegalStateException("server error"))
//        val actual = repository.tickets(boardId = "stub").toList()
//
//        assertEquals(emptyList<TicketResult>(), actual)
//    }
//
//    @Test
//    fun test_move_ticket() = runBlocking {
//        repository.moveTicket(ticketId = "move ticket id", column = Column.Done)
//        assertEquals(1, ticketRemoteDataSource.moveTicketCalledList.size)
//        assertEquals(
//            Pair("move ticket id", Column.Done),
//            ticketRemoteDataSource.moveTicketCalledList[0]
//        )
//    }
//
//    @Test
//    fun test_leave_board() = runBlocking {
//        repository.leaveBoard(boardId = "testBoardId")
//        assertEquals(1, boardRemoteDataSource.leaveBoardCalledList.size)
//        assertEquals("testBoardId", boardRemoteDataSource.leaveBoardCalledList[0])
//    }
//}