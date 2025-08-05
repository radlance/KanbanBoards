package com.github.radlance.kanbanboards.board.core.presentation

import com.github.radlance.kanbanboards.board.core.BaseBoardCoreTest
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import com.github.radlance.kanbanboards.core.domain.BoardInfo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BoardViewModelTest : BaseBoardCoreTest() {

    private lateinit var repository: TestBoardRepository
    private lateinit var handle: TestHandleBoard

    private lateinit var viewModel: BoardViewModel

    @Before
    fun setup() {
        repository = TestBoardRepository()
        handle = TestHandleBoard()

        viewModel = BoardViewModel(
            boardRepository = repository,
            handleBoard = handle,
            facade = BaseBoardMapperFacade(
                boardResultMapper = BoardResultMapper(),
                ticketResultMapper = TicketResultMapper(ColumnMapper()),
                columnUiMapper = ColumnUiMapper()
            ),
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(BoardUiState.Loading, viewModel.boardUiState.value)
        assertEquals(1, handle.boardUiStateCalledCount)
        assertEquals(0, handle.saveBoardUiStateCalledList.size)

        assertEquals(TicketBoardUiState.Loading, viewModel.ticketBoardUiState.value)
        assertEquals(1, handle.ticketUiStateCalledCount)
        assertEquals(0, handle.saveTicketUiStateCalledList.size)
    }

    @Test
    fun test_collect_board() {
        repository.makeExpectedBoardResult(BoardResult.Error(message = "error loading board"))
        viewModel.fetchBoard(
            boardInfo = BoardInfo(id = "test id", name = "test name", isMyBoard = true)
        )

        assertEquals(
            BoardUiState.Error(message = "error loading board"),
            viewModel.boardUiState.value
        )
        assertEquals(1, repository.boardCalledList.size)
        assertEquals("test id", repository.boardCalledList[0])
        assertEquals(2, handle.saveBoardUiStateCalledList.size)
        assertEquals(
            BoardUiState.Success(BoardInfo(id = "test id", name = "test name", isMyBoard = true)),
            handle.saveBoardUiStateCalledList[0]
        )
        assertEquals(
            BoardUiState.Error(message = "error loading board"),
            handle.saveBoardUiStateCalledList[1]
        )

        repository.makeExpectedBoardResult(
            BoardResult.Success(
                boardInfo = BoardInfo(id = "first", name = "example", isMyBoard = false)
            )
        )
        assertEquals(
            BoardUiState.Success(
                BoardInfo(id = "first", name = "example", isMyBoard = false)
            ), viewModel.boardUiState.value
        )
        assertEquals(1, repository.boardCalledList.size)
        assertEquals(3, handle.saveBoardUiStateCalledList.size)
        assertEquals(
            BoardUiState.Success(
                BoardInfo(id = "first", name = "example", isMyBoard = false)
            ),
            handle.saveBoardUiStateCalledList[2]
        )

        repository.makeExpectedBoardResult(BoardResult.NotExists)
        assertEquals(BoardUiState.NotExists, viewModel.boardUiState.value)
        assertEquals(1, repository.boardCalledList.size)
        assertEquals(4, handle.saveBoardUiStateCalledList.size)
        assertEquals(
            BoardUiState.NotExists,
            handle.saveBoardUiStateCalledList[3]
        )
    }

    @Test
    fun test_collect_tickets() {
        repository.makeExpectedTicketResult(TicketResult.Error(message = "error loading tickets"))
        viewModel.fetchTickets(boardId = "test board id")
        assertEquals(
            TicketBoardUiState.Error(message = "error loading tickets"),
            viewModel.ticketBoardUiState.value
        )
        assertEquals(1, repository.ticketsCalledList.size)
        assertEquals("test board id", repository.ticketsCalledList[0])
        assertEquals(1, handle.saveTicketUiStateCalledList.size)
        assertEquals(
            TicketBoardUiState.Error(message = "error loading tickets"),
            handle.saveTicketUiStateCalledList[0]
        )

        repository.makeExpectedTicketResult(
            TicketResult.Success(
                tickets = listOf(
                    Ticket(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        assignedMemberNames = listOf("test user"),
                        description = "test description",
                        column = Column.Todo,
                        creationDate = LocalDateTime.of(2024, 6, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id")
                    ),

                    Ticket(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberNames = emptyList(),
                        column = Column.InProgress,
                        creationDate = LocalDateTime.of(2024, 5, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id2")
                    ),

                    Ticket(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberNames = listOf("another user"),
                        column = Column.Done,
                        creationDate = LocalDateTime.of(2024, 4, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id3")
                    )
                )
            )
        )
        assertEquals(
            TicketBoardUiState.Success(
                listOf(
                    TicketUi(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        description = "test description",
                        assignedMemberNames = listOf("test user"),
                        column = ColumnUi.Todo,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 6, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id")
                    ),

                    TicketUi(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberNames = emptyList(),
                        column = ColumnUi.InProgress,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 5, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id2")
                    ),

                    TicketUi(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberNames = listOf("another user"),
                        column = ColumnUi.Done,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 4, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id3")
                    )
                )
            ),
            viewModel.ticketBoardUiState.value
        )
        assertEquals(1, repository.ticketsCalledList.size)
        assertEquals(2, handle.saveTicketUiStateCalledList.size)
        assertEquals(
            TicketBoardUiState.Success(
                listOf(
                    TicketUi(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        description = "test description",
                        assignedMemberNames = listOf("test user"),
                        column = ColumnUi.Todo,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 6, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id")
                    ),

                    TicketUi(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberNames = emptyList(),
                        column = ColumnUi.InProgress,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 5, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id2")
                    ),

                    TicketUi(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberNames = listOf("another user"),
                        column = ColumnUi.Done,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 4, 18, 6, 30),
                        assignedMemberIds = listOf("test assigned member id3")
                    )
                )
            ),
            handle.saveTicketUiStateCalledList[1]
        )
    }

    @Test
    fun test_move_ticket() {
        viewModel.moveTicket(ticketId = "ticket id", column = ColumnUi.InProgress)
        assertEquals(1, repository.moveTicketCalledList.size)
        assertEquals(Pair("ticket id", Column.InProgress), repository.moveTicketCalledList[0])

        viewModel.moveTicket(ticketId = "second id", column = ColumnUi.Done)
        assertEquals(2, repository.moveTicketCalledList.size)
        assertEquals(Pair("second id", Column.Done), repository.moveTicketCalledList[1])

        viewModel.moveTicket(ticketId = "new id", column = ColumnUi.Todo)
        assertEquals(3, repository.moveTicketCalledList.size)
        assertEquals(Pair("new id", Column.Todo), repository.moveTicketCalledList[2])
    }

    @Test
    fun test_leave_board() {
        viewModel.leaveBoard(boardId = "boardId")
        assertEquals(1, repository.leaveBoardCalledList.size)
        assertEquals("boardId", repository.leaveBoardCalledList[0])
    }

    private class TestHandleBoard : HandleBoard {

        private val boardUiStateMutable = MutableStateFlow<BoardUiState>(BoardUiState.Loading)
        var boardUiStateCalledCount = 0
        var saveBoardUiStateCalledList = mutableListOf<BoardUiState>()

        private val ticketUiStateMutable =
            MutableStateFlow<TicketBoardUiState>(TicketBoardUiState.Loading)
        var ticketUiStateCalledCount = 0
        var saveTicketUiStateCalledList = mutableListOf<TicketBoardUiState>()

        override val boardUiState: StateFlow<BoardUiState>
            get() {
                boardUiStateCalledCount++
                return boardUiStateMutable
            }

        override fun saveBoardUiState(boardUiState: BoardUiState) {
            saveBoardUiStateCalledList.add(boardUiState)
            this.boardUiStateMutable.value = boardUiState
        }

        override val ticketBoardUiState: StateFlow<TicketBoardUiState>
            get() {
                ticketUiStateCalledCount++
                return ticketUiStateMutable
            }

        override fun saveTicketUiState(ticketBoardUiState: TicketBoardUiState) {
            saveTicketUiStateCalledList.add(ticketBoardUiState)
            this.ticketUiStateMutable.value = ticketBoardUiState
        }
    }
}