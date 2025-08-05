package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.core.domain.User
import com.github.radlance.kanbanboards.ticket.core.TestBaseHandleTicket
import com.github.radlance.kanbanboards.ticket.core.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.create.data.TestCreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.data.TestHandleCreateTicket
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TicketViewModelTest : BaseTest() {

    private lateinit var repository: TestCreateTicketRepository
    private lateinit var baseHandle: TestBaseHandleTicket
    private lateinit var handleCreateTicket: TestHandleCreateTicket
    private lateinit var formatTime: TestFormatTime

    private lateinit var viewModel: CreateTicketViewModel

    @Before
    fun setup() {
        repository = TestCreateTicketRepository()
        baseHandle = TestBaseHandleTicket()
        handleCreateTicket = TestHandleCreateTicket(baseHandle)
        formatTime = TestFormatTime()

        viewModel = CreateTicketViewModel(
            createTicketRepository = repository,
            createTicketMapperFacade = BaseCreateTicketMapperFacade(
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
            assigneeIds = listOf("assignee1"),
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
                assignedMemberIds = listOf("assignee1"),
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
            assigneeIds = listOf("assignee2"),
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
                assignedMemberIds = listOf("assignee2"),
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

    private class TestFormatTime : FormatTime {

        private var time = LocalDateTime.of(2024, 6, 6, 6, 6)

        fun makeExpectedTime(localDateTime: LocalDateTime) {
            time = localDateTime
        }

        override fun now(): LocalDateTime = time
    }
}