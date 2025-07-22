package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class EditTicketViewModelTest : BaseTest() {

    private lateinit var repository: TestEditTicketRepository
    private lateinit var ticketRepository: TestTicketRepository
    private lateinit var ticketInfoRepository: TestTicketInfoRepository
    private lateinit var baseHandle: TestBaseHandleTicket
    private lateinit var handle: TestHandleEditTicket
    private lateinit var viewModel: EditTicketViewModel

    @Before
    fun setup() {
        ticketRepository = TestTicketRepository()
        ticketInfoRepository = TestTicketInfoRepository()

        repository = TestEditTicketRepository(
            ticketRepository = ticketRepository,
            ticketInfoRepository = ticketInfoRepository
        )

        baseHandle = TestBaseHandleTicket()
        handle = TestHandleEditTicket(testBaseHandleTicket = baseHandle)

        viewModel = EditTicketViewModel(
            editTicketRepository = repository,
            editTicketMapperFacade = EditTicketMapperFacade.Base(
                boardMembersMapper = BoardMembersEditMapper(),
                ticketInfoUiMapper = TicketInfoEditMapper(),
                createTicketMapper = CreateTicketMapper()
            ),
            handleEditTicket = handle,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(BoardMembersUiStateEdit.Loading, viewModel.boardMembersUiState.value)
        assertEquals(TicketUiState.Initial, viewModel.ticketUiState.value)
        assertEquals(TicketInfoEditUiState.Loading, viewModel.ticketInfoUiState.value)
        assertEquals(1, handle.boardMembersUiStateCalledCount)
        assertEquals(1, baseHandle.ticketUiStateCalledCount)
        assertEquals(1, handle.ticketInfoEditUiStateCalledCount)
    }

    @Test
    fun test_collect_board_members() {
        ticketRepository.makeExpectedBoardMembersResult(
            BoardMembersResult.Error(message = "board members error")
        )
        viewModel.fetchBoardMembers(boardId = "test board id", ownerId = "test owner id")
        assertEquals(
            BoardMembersUiStateEdit.Error(message = "board members error"),
            viewModel.boardMembersUiState.value
        )
        assertEquals(1, ticketRepository.boardMembersCalledList.size)
        assertEquals("test board id", ticketRepository.boardMembersCalledList[0].first)
        assertEquals("test owner id", ticketRepository.boardMembersCalledList[0].second)
        assertEquals(1, handle.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiStateEdit.Error(message = "board members error"),
            handle.saveBoardMembersUiStateCalledList[0]
        )

        ticketRepository.makeExpectedBoardMembersResult(
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
            BoardMembersUiStateEdit.Success(
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
        assertEquals(1, ticketRepository.boardMembersCalledList.size)
        assertEquals(2, handle.saveBoardMembersUiStateCalledList.size)
        assertEquals(
            BoardMembersUiStateEdit.Success(
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
    fun test_collect_ticket() {
        ticketInfoRepository.makeExpectedTicketInfoResult(
            TicketInfoResult.Error(message = "error loading ticket")
        )
        viewModel.fetchTicket(ticketId = "123")
        assertEquals(1, ticketInfoRepository.ticketCalledList.size)
        assertEquals("123", ticketInfoRepository.ticketCalledList[0])
        assertEquals(1, handle.saveTicketInfoEditUiStateCalledList.size)
        assertEquals(
            TicketInfoEditUiState.Error(message = "error loading ticket"),
            handle.saveTicketInfoEditUiStateCalledList[0]
        )

        ticketInfoRepository.makeExpectedTicketInfoResult(
            TicketInfoResult.Success(
                ticket = Ticket(
                    colorHex = "#FFFFFF",
                    id = "success id",
                    name = "success name",
                    description = "success description",
                    assignedMemberName = "success assignee member name",
                    column = Column.InProgress,
                    creationDate = LocalDateTime.of(
                        2024,
                        2,
                        2,
                        2,
                        2
                    ),
                    assignedMemberId = "success assigned member id"
                )
            )
        )
        assertEquals(1, ticketInfoRepository.ticketCalledList.size)
        assertEquals(2, handle.saveTicketInfoEditUiStateCalledList.size)
        assertEquals(
            TicketInfoEditUiState.Success(
                ticket = Ticket(
                    colorHex = "#FFFFFF",
                    id = "success id",
                    name = "success name",
                    description = "success description",
                    assignedMemberName = "success assignee member name",
                    column = Column.InProgress,
                    creationDate = LocalDateTime.of(
                        2024,
                        2,
                        2,
                        2,
                        2
                    ),
                    assignedMemberId = "success assigned member id"
                )
            ),
            handle.saveTicketInfoEditUiStateCalledList[1]
        )
    }

    @Test
    fun test_collect_edit_ticket_ui_state() {
        repository.makeExpectedEditTicketResult(UnitResult.Error(message = "create ticket error"))
        viewModel.action(
            boardId = "boardId1",
            title = "title1",
            color = "#000000",
            description = "description1",
            assigneeId = "assignee1",
            ticketId = "ticketId1",
            column = Column.Todo,
            creationDate = LocalDateTime.of(2025, 3, 3, 3, 3)
        )
        assertEquals(
            TicketUiState.Error(message = "create ticket error"),
            viewModel.ticketUiState.value
        )
        assertEquals(1, repository.editTicketCalledList.size)
        assertEquals(
            EditTicket(
                boardId = "boardId1",
                colorHex = "#000000",
                name = "title1",
                description = "description1",
                assignedMemberId = "assignee1",
                creationDate = LocalDateTime.of(2025, 3, 3, 3, 3),
                id = "ticketId1",
                column = Column.Todo
            ),
            repository.editTicketCalledList[0]
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

        repository.makeExpectedEditTicketResult(UnitResult.Success)
        viewModel.action(
            boardId = "boardId2",
            title = "title2",
            color = "#000000",
            description = "description2",
            assigneeId = "assignee2",
            ticketId = "ticketId2",
            column = Column.InProgress,
            creationDate = LocalDateTime.of(2024, 1, 1, 1, 1)
        )
        assertEquals(
            TicketUiState.Success,
            viewModel.ticketUiState.value
        )
        assertEquals(2, repository.editTicketCalledList.size)
        assertEquals(
            EditTicket(
                boardId = "boardId2",
                colorHex = "#000000",
                name = "title2",
                description = "description2",
                assignedMemberId = "assignee2",
                creationDate = LocalDateTime.of(2024, 1, 1, 1, 1),
                id = "ticketId2",
                column = Column.InProgress
            ),
            repository.editTicketCalledList[1]
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

    private class TestEditTicketRepository(
        private val ticketRepository: TestTicketRepository,
        private val ticketInfoRepository: TestTicketInfoRepository
    ) : EditTicketRepository {

        val editTicketCalledList = mutableListOf<EditTicket>()
        private var editTicketResult: UnitResult = UnitResult.Error(message = "initial result")

        fun makeExpectedEditTicketResult(editTicketResult: UnitResult) {
            this.editTicketResult = editTicketResult
        }

        override suspend fun editTicket(ticket: EditTicket): UnitResult {
            editTicketCalledList.add(ticket)
            return editTicketResult
        }

        override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
            return ticketRepository.boardMembers(boardId, ownerId)
        }

        override fun ticket(ticketId: String): Flow<TicketInfoResult> {
            return ticketInfoRepository.ticket(ticketId)
        }
    }

    private class TestHandleEditTicket(
        private val testBaseHandleTicket: TestBaseHandleTicket
    ) : HandleEditTicket {

        var boardMembersUiStateCalledCount = 0
        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiStateEdit>(
            BoardMembersUiStateEdit.Loading
        )
        val saveBoardMembersUiStateCalledList = mutableListOf<BoardMembersUiStateEdit>()

        var ticketInfoEditUiStateCalledCount = 0
        private val ticketInfoEditUiStateMutable = MutableStateFlow<TicketInfoEditUiState>(
            TicketInfoEditUiState.Loading
        )
        val saveTicketInfoEditUiStateCalledList = mutableListOf<TicketInfoEditUiState>()

        override val ticketUiState: StateFlow<TicketUiState>
            get() = testBaseHandleTicket.ticketUiState

        override val boardMembersUiState: StateFlow<BoardMembersUiStateEdit>
            get() {
                boardMembersUiStateCalledCount++
                return boardMembersUiStateMutable
            }

        override fun saveBoardMembersUiState(boardMembersUiStateEdit: BoardMembersUiStateEdit) {
            saveBoardMembersUiStateCalledList.add(boardMembersUiStateEdit)
            boardMembersUiStateMutable.value = boardMembersUiStateEdit
        }

        override val ticketInfoEditUiState: StateFlow<TicketInfoEditUiState>
            get() {
                ticketInfoEditUiStateCalledCount++
                return ticketInfoEditUiStateMutable
            }

        override fun saveTicketInfoEditUiState(ticketInfoEditUiState: TicketInfoEditUiState) {
            saveTicketInfoEditUiStateCalledList.add(ticketInfoEditUiState)
            ticketInfoEditUiStateMutable.value = ticketInfoEditUiState
        }

        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            testBaseHandleTicket.saveTicketUiState(ticketUiState)
        }
    }
}