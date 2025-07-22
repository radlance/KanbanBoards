package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TicketViewModelTest : BaseTest() {

    private lateinit var repository: TestTicketRepository
    private lateinit var baseHandle: TestBaseHandleTicket
    private lateinit var handleCreateTicket: TestHandleCreateTicket
    private lateinit var formatTime: TestFormatTime

    private lateinit var viewModel: CreateTicketViewModel

    @Before
    fun setup() {
        repository = TestTicketRepository()
        baseHandle = TestBaseHandleTicket()
        handleCreateTicket = TestHandleCreateTicket(baseHandle)
        formatTime = TestFormatTime()

        viewModel = CreateTicketViewModel(
            createTicketRepository = repository,
            createTicketMapperFacade = CreateTicketMapperFacade.Base(
                boardMembersMapper = BoardMembersResultMapper(),
                createTicketMapper = CreateTicketMapper()
            ),
            handleTicket = handleCreateTicket,
            formatTime = formatTime,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(BoardMembersUiStateCreate.Loading, viewModel.boardMembersUiState.value)
        assertEquals(TicketUiState.Initial, viewModel.ticketUiState.value)
        assertEquals(1, handleCreateTicket.boardMembersUiStateCalledCount)
        assertEquals(1, baseHandle.ticketUiStateCalledCount)
    }

    @Test
    fun test_collect_board_members() {
        repository.makeExpectedBoardMembersResult(
            BoardMembersResult.Error(message = "board members error")
        )
        viewModel.fetchBoardMembers(boardId = "test board id", ownerId = "test owner id")
        assertEquals(
            BoardMembersUiStateCreate.Error(message = "board members error"),
            viewModel.boardMembersUiState.value
        )
        assertEquals(1, repository.boardMembersCalledList.size)
        assertEquals("test board id", repository.boardMembersCalledList[0].first)
        assertEquals("test owner id", repository.boardMembersCalledList[0].second)
        assertEquals(1, handleCreateTicket.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiStateCreate.Error(message = "board members error"),
            handleCreateTicket.saveBoardMembersUiStateCalledList[0]
        )

        repository.makeExpectedBoardMembersResult(
            BoardMembersResult.Success(
                members = listOf(
                    User(
                        id = "boardMemberId",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            )
        )
        assertEquals(
            BoardMembersUiStateCreate.Success(
                members = listOf(
                    User(
                        id = "boardMemberId",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            ),
            viewModel.boardMembersUiState.value
        )
        assertEquals(1, repository.boardMembersCalledList.size)
        assertEquals(2, handleCreateTicket.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiStateCreate.Success(
                members = listOf(
                    User(
                        id = "boardMemberId",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            ),
            handleCreateTicket.saveBoardMembersUiStateCalledList[1]
        )
    }

    @Test
    fun test_collect_create_ticket_ui_state() {
        formatTime.makeExpectedTime(LocalDateTime.of(2025, 1, 1, 1, 1))
        repository.makeExpectedCreateTicketResult(UnitResult.Error(message = "create ticket error"))
        viewModel.action(
            boardId = "boardId1",
            title = "title1",
            color = "#000000",
            description = "description1",
            assigneeId = "assignee1",
            ticketId = "stub",
            column = Column.Todo,
            creationDate = LocalDateTime.of(1, 1, 1, 1, 1)
        )
        assertEquals(
            TicketUiState.Error(message = "create ticket error"),
            viewModel.ticketUiState.value
        )
        assertEquals(1, repository.createTicketCalledList.size)
        assertEquals(
            NewTicket(
                boardId = "boardId1",
                colorHex = "#000000",
                name = "title1",
                description = "description1",
                assignedMemberId = "assignee1",
                creationDate = LocalDateTime.of(2025, 1, 1, 1, 1)
            ),
            repository.createTicketCalledList[0]
        )
        assertEquals(2, baseHandle.saveCreateTicketUiStateCalledList.size)

        assertEquals(
            TicketUiState.Loading,
            baseHandle.saveCreateTicketUiStateCalledList[0]
        )

        assertEquals(
            TicketUiState.Error(message = "create ticket error"),
            baseHandle.saveCreateTicketUiStateCalledList[1]
        )

        formatTime.makeExpectedTime(LocalDateTime.of(2025, 1, 1, 1, 30))
        repository.makeExpectedCreateTicketResult(UnitResult.Success)
        viewModel.action(
            boardId = "boardId2",
            title = "title2",
            color = "#000000",
            description = "description2",
            assigneeId = "assignee2",
            ticketId = "stub",
            column = Column.Todo,
            creationDate = LocalDateTime.of(1, 1, 1, 1, 1)
        )
        assertEquals(
            TicketUiState.Success,
            viewModel.ticketUiState.value
        )
        assertEquals(2, repository.createTicketCalledList.size)
        assertEquals(
            NewTicket(
                boardId = "boardId2",
                colorHex = "#000000",
                name = "title2",
                description = "description2",
                assignedMemberId = "assignee2",
                creationDate = LocalDateTime.of(2025, 1, 1, 1, 30)
            ),
            repository.createTicketCalledList[1]
        )
        assertEquals(4, baseHandle.saveCreateTicketUiStateCalledList.size)
        assertEquals(
            TicketUiState.Loading,
            baseHandle.saveCreateTicketUiStateCalledList[2]
        )
        assertEquals(
            TicketUiState.Success,
            baseHandle.saveCreateTicketUiStateCalledList[3]
        )
    }

    @Test
    fun test_clear_create_ticket_ui_state() {
        repository.makeExpectedCreateTicketResult(UnitResult.Success)
        handleCreateTicket.saveTicketUiState(TicketUiState.Success)
        assertEquals(
            TicketUiState.Success,
            viewModel.ticketUiState.value
        )
        assertEquals(1, baseHandle.saveCreateTicketUiStateCalledList.size)
        assertEquals(TicketUiState.Success, baseHandle.saveCreateTicketUiStateCalledList[0])

        viewModel.clearCreateTicketUiState()
        assertEquals(
            TicketUiState.Initial,
            viewModel.ticketUiState.value
        )
        assertEquals(2, baseHandle.saveCreateTicketUiStateCalledList.size)
        assertEquals(TicketUiState.Initial, baseHandle.saveCreateTicketUiStateCalledList[1])
    }

    private class TestHandleCreateTicket(
        private val testBaseHandleTicket: TestBaseHandleTicket
    ) : HandleCreateTicket {
        private val boardMembersUiStateMutable =
            MutableStateFlow<BoardMembersUiStateCreate>(BoardMembersUiStateCreate.Loading)
        var boardMembersUiStateCalledCount = 0
        val saveBoardMembersUiStateCalledList = mutableListOf<BoardMembersUiStateCreate>()

        override val boardMembersUiState: StateFlow<BoardMembersUiStateCreate>
            get() {
                boardMembersUiStateCalledCount++
                return boardMembersUiStateMutable
            }

        override fun saveBoardMembersUiState(boardMembersUiStateCreate: BoardMembersUiStateCreate) {
            saveBoardMembersUiStateCalledList.add(boardMembersUiStateCreate)
            boardMembersUiStateMutable.value = boardMembersUiStateCreate
        }

        override val ticketUiState: StateFlow<TicketUiState>
            get() = testBaseHandleTicket.ticketUiState

        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            testBaseHandleTicket.saveTicketUiState(ticketUiState)
        }
    }

    private class TestFormatTime : FormatTime {

        private var time = LocalDateTime.of(2024, 6, 6, 6, 6)

        fun makeExpectedTime(localDateTime: LocalDateTime) {
            time = localDateTime
        }

        override fun now(): LocalDateTime = time
    }
}