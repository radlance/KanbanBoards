package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TicketViewModelTest : BaseTest() {

    private lateinit var repository: TestTicketRepository
    private lateinit var handle: TestHandleTicket
    private lateinit var formatTime: TestFormatTime

    private lateinit var viewModel: TicketViewModel

    @Before
    fun setup() {
        repository = TestTicketRepository()
        handle = TestHandleTicket()
        formatTime = TestFormatTime()

        viewModel = TicketViewModel(
            ticketRepository = repository,
            ticketMapperFacade = TicketMapperFacade.Base(
                boardMembersMapper = BoardMembersResultMapper(),
                createTicketMapper = CreateTicketMapper()
            ),
            handleTicket = handle,
            formatTime = formatTime,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(BoardMembersUiState.Loading, viewModel.boardMembersUiState.value)
        assertEquals(CreateTicketUiState.Initial, viewModel.createTicketUiState.value)
        assertEquals(1, handle.boardMembersUiStateCalledCount)
        assertEquals(1, handle.createTicketUiStateCalledCount)
    }

    @Test
    fun test_collect_board_members() {
        repository.makeExpectedBoardMembersResult(
            BoardMembersResult.Error(message = "board members error")
        )
        viewModel.fetchBoardMembers(boardId = "test board id")
        assertEquals(
            BoardMembersUiState.Error(message = "board members error"),
            viewModel.boardMembersUiState.value
        )
        assertEquals(1, repository.boardMembersCalledList.size)
        assertEquals("test board id", repository.boardMembersCalledList[0])
        assertEquals(1, handle.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiState.Error(message = "board members error"),
            handle.saveBoardMembersUiStateCalledList[0]
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
            BoardMembersUiState.Success(
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
        assertEquals(2, handle.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiState.Success(
                members = listOf(
                    User(
                        id = "boardMemberId",
                        email = "test@gmail.com",
                        name = "name"
                    )
                )
            ),
            handle.saveBoardMembersUiStateCalledList[1]
        )
    }

    @Test
    fun test_collect_create_ticket_ui_state() {
        formatTime.makeExpectedTime(LocalDateTime.of(2025, 1, 1, 1, 1))
        repository.makeExpectedCreateTicketResult(UnitResult.Error(message = "create ticket error"))
        viewModel.createTicket(
            boardId = "boardId1",
            title = "title1",
            color = "#000000",
            description = "description1",
            assigneeId = "assignee1"
        )
        assertEquals(
            CreateTicketUiState.Error(message = "create ticket error"),
            viewModel.createTicketUiState.value
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
        assertEquals(2, handle.saveCreateTicketUiStateCalledList.size)

        assertEquals(
            CreateTicketUiState.Loading,
            handle.saveCreateTicketUiStateCalledList[0]
        )

        assertEquals(
            CreateTicketUiState.Error(message = "create ticket error"),
            handle.saveCreateTicketUiStateCalledList[1]
        )

        formatTime.makeExpectedTime(LocalDateTime.of(2025, 1, 1, 1, 30))
        repository.makeExpectedCreateTicketResult(UnitResult.Success)
        viewModel.createTicket(
            boardId = "boardId2",
            title = "title2",
            color = "#000000",
            description = "description2",
            assigneeId = "assignee2"
        )
        assertEquals(
            CreateTicketUiState.Success,
            viewModel.createTicketUiState.value
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
        assertEquals(4, handle.saveCreateTicketUiStateCalledList.size)
        assertEquals(
            CreateTicketUiState.Loading,
            handle.saveCreateTicketUiStateCalledList[2]
        )
        assertEquals(
            CreateTicketUiState.Success,
            handle.saveCreateTicketUiStateCalledList[3]
        )
    }

    @Test
    fun test_clear_create_ticket_ui_state() {
        repository.makeExpectedCreateTicketResult(UnitResult.Success)
        handle.saveCreateTicketUiState(CreateTicketUiState.Success)
        assertEquals(
            CreateTicketUiState.Success,
            viewModel.createTicketUiState.value
        )
        assertEquals(1, handle.saveCreateTicketUiStateCalledList.size)
        assertEquals(CreateTicketUiState.Success, handle.saveCreateTicketUiStateCalledList[0])

        viewModel.clearCreateTicketUiState()
        assertEquals(
            CreateTicketUiState.Initial,
            viewModel.createTicketUiState.value
        )
        assertEquals(2, handle.saveCreateTicketUiStateCalledList.size)
        assertEquals(CreateTicketUiState.Initial, handle.saveCreateTicketUiStateCalledList[1])
    }

    private class TestTicketRepository : TicketRepository {

        val boardMembersCalledList = mutableListOf<String>()
        private val boardMembersResult = MutableStateFlow<BoardMembersResult>(
            BoardMembersResult.Success(members = emptyList())
        )

        fun makeExpectedBoardMembersResult(boardMembersResult: BoardMembersResult) {
            this.boardMembersResult.value = boardMembersResult
        }

        val createTicketCalledList = mutableListOf<NewTicket>()
        private var createTicketResult: UnitResult = UnitResult.Success
        fun makeExpectedCreateTicketResult(createTicketResult: UnitResult) {
            this.createTicketResult = createTicketResult
        }

        override fun boardMembers(boardId: String): Flow<BoardMembersResult> {
            boardMembersCalledList.add(boardId)
            return boardMembersResult
        }

        override suspend fun createTicket(newTicket: NewTicket): UnitResult {
            createTicketCalledList.add(newTicket)
            return createTicketResult
        }
    }

    private class TestHandleTicket : HandleTicket {
        private val boardMembersUiStateMutable =
            MutableStateFlow<BoardMembersUiState>(BoardMembersUiState.Loading)
        var boardMembersUiStateCalledCount = 0
        val saveBoardMembersUiStateCalledList = mutableListOf<BoardMembersUiState>()

        private val createTicketUiStateMutable =
            MutableStateFlow<CreateTicketUiState>(CreateTicketUiState.Initial)
        var createTicketUiStateCalledCount = 0
        val saveCreateTicketUiStateCalledList = mutableListOf<CreateTicketUiState>()

        override val boardMembersUiState: StateFlow<BoardMembersUiState>
            get() {
                boardMembersUiStateCalledCount++
                return boardMembersUiStateMutable
            }

        override fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState) {
            saveBoardMembersUiStateCalledList.add(boardMembersUiState)
            boardMembersUiStateMutable.value = boardMembersUiState
        }

        override val createTicketUiState: StateFlow<CreateTicketUiState>
            get() {
                createTicketUiStateCalledCount++
                return createTicketUiStateMutable
            }

        override fun saveCreateTicketUiState(createTicketUiState: CreateTicketUiState) {
            saveCreateTicketUiStateCalledList.add(createTicketUiState)
            createTicketUiStateMutable.value = createTicketUiState
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