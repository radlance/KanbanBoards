package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.board.domain.TicketResult
import com.github.radlance.kanbanboards.common.BaseTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BoardViewModelTest : BaseTest() {

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
            facade = BoardMapperFacade.Base(
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

        assertEquals(TicketUiState.Loading, viewModel.ticketUiState.value)
        assertEquals(1, handle.ticketUiStateCalledCount)
        assertEquals(0, handle.saveTicketUiStateCalledList.size)
    }

    @Test
    fun test_collect_board() {
        repository.makeExpectedBoardResult(BoardResult.Error(message = "error loading board"))
        viewModel.fetchBoard(
            boardInfo = BoardInfo(id = "test id", name = "test name", isMyBoard = true)
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
        assertEquals(1, repository.boardCalledList.size)
        assertEquals(3, handle.saveBoardUiStateCalledList.size)
        assertEquals(
            BoardUiState.Success(
                BoardInfo(id = "first", name = "example", isMyBoard = false)
            ),
            handle.saveBoardUiStateCalledList[2]
        )
    }

    @Test
    fun test_collect_tickets() {
        repository.makeExpectedTicketResult(TicketResult.Error(message = "error loading tickets"))
        viewModel.fetchTickets(boardId = "test board id")
        assertEquals(
            TicketUiState.Error(message = "error loading tickets"),
            viewModel.ticketUiState.value
        )
        assertEquals(1, repository.ticketsCalledList.size)
        assertEquals("test board id", repository.ticketsCalledList[0])
        assertEquals(1, handle.saveTicketUiStateCalledList.size)
        assertEquals(
            TicketUiState.Error(message = "error loading tickets"),
            handle.saveTicketUiStateCalledList[0]
        )

        repository.makeExpectedTicketResult(
            TicketResult.Success(
                tickets = listOf(
                    Ticket(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        assignedMemberName = "test user",
                        description = "test description",
                        column = Column.Todo,
                        creationDate = LocalDateTime.of(2024, 6, 18, 6, 30)
                    ),

                    Ticket(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberName = "",
                        column = Column.InProgress,
                        creationDate = LocalDateTime.of(2024, 5, 18, 6, 30)
                    ),

                    Ticket(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberName = "another user",
                        column = Column.Done,
                        creationDate = LocalDateTime.of(2024, 4, 18, 6, 30)
                    )
                )
            )
        )
        assertEquals(
            TicketUiState.Success(
                listOf(
                    TicketUi(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        description = "test description",
                        assignedMemberName = "test user",
                        column = ColumnUi.Todo,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 6, 18, 6, 30)
                    ),

                    TicketUi(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberName = "",
                        column = ColumnUi.InProgress,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 5, 18, 6, 30)
                    ),

                    TicketUi(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberName = "another user",
                        column = ColumnUi.Done,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 4, 18, 6, 30)
                    )
                )
            ),
            viewModel.ticketUiState.value
        )
        assertEquals(1, repository.ticketsCalledList.size)
        assertEquals(2, handle.saveTicketUiStateCalledList.size)
        assertEquals(
            TicketUiState.Success(
                listOf(
                    TicketUi(
                        id = "test ticket id",
                        colorHex = "#FFFFFF",
                        name = "test ticket name",
                        description = "test description",
                        assignedMemberName = "test user",
                        column = ColumnUi.Todo,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 6, 18, 6, 30)
                    ),

                    TicketUi(
                        id = "second ticket id",
                        colorHex = "#FAEEFF",
                        name = "test task",
                        description = "test",
                        assignedMemberName = "",
                        column = ColumnUi.InProgress,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 5, 18, 6, 30)
                    ),

                    TicketUi(
                        id = "id",
                        colorHex = "#000000",
                        name = "first task",
                        description = "",
                        assignedMemberName = "another user",
                        column = ColumnUi.Done,
                        creationDate = kotlinx.datetime.LocalDateTime(2024, 4, 18, 6, 30)
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

    private class TestBoardRepository : BoardRepository {
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
    }

    private class TestHandleBoard : HandleBoard {

        private val boardUiStateMutable = MutableStateFlow<BoardUiState>(BoardUiState.Loading)
        var boardUiStateCalledCount = 0
        var saveBoardUiStateCalledList = mutableListOf<BoardUiState>()

        private val ticketUiStateMutable = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
        var ticketUiStateCalledCount = 0
        var saveTicketUiStateCalledList = mutableListOf<TicketUiState>()

        override val boardUiState: StateFlow<BoardUiState>
            get() {
            boardUiStateCalledCount++
                return boardUiStateMutable
        }

        override fun saveBoardUiState(boardUiState: BoardUiState) {
            saveBoardUiStateCalledList.add(boardUiState)
            this.boardUiStateMutable.value = boardUiState
        }

        override val ticketUiState: StateFlow<TicketUiState>
            get() {
            ticketUiStateCalledCount++
                return ticketUiStateMutable
        }

        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            saveTicketUiStateCalledList.add(ticketUiState)
            this.ticketUiStateMutable.value = ticketUiState
        }
    }
}