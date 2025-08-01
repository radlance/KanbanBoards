//package com.github.radlance.kanbanboards.ticket.info.presentation
//
//import com.github.radlance.kanbanboards.board.core.domain.Column
//import com.github.radlance.kanbanboards.board.core.domain.Ticket
//import com.github.radlance.kanbanboards.board.core.presentation.ColumnUi
//import com.github.radlance.kanbanboards.board.core.presentation.ColumnUiMapper
//import com.github.radlance.kanbanboards.board.core.presentation.TicketUi
//import com.github.radlance.kanbanboards.common.BaseTest
//import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.datetime.LocalDateTime
//import org.junit.Before
//import org.junit.Test
//
//class TicketInfoViewModelTest : BaseTest() {
//
//    private lateinit var repository: TestTicketInfoRepository
//    private lateinit var handle: TestHandleTicketInfo
//
//    private lateinit var viewModel: TicketInfoViewModel
//
//    @Before
//    fun setup() {
//        repository = TestTicketInfoRepository()
//        handle = TestHandleTicketInfo()
//
//        viewModel = TicketInfoViewModel(
//            ticketInfoRepository = repository,
//            facade = TicketInfoFacade.Base(
//                ticketInfoMapper = TicketInfoResultMapper(),
//                columnUiMapper = ColumnUiMapper()
//            ),
//            handleTicketInfo = handle,
//            runAsync = TestRunAsync()
//        )
//    }
//
//    @Test
//    fun test_initial_state() {
//        assertEquals(TicketInfoUiState.Loading, viewModel.ticketInfoUiState.value)
//        assertEquals(1, handle.ticketInfoUiStateCalledCount)
//    }
//
//    @Test
//    fun test_collect_ticket() {
//        repository.makeExpectedTicketInfoResult(
//            TicketInfoResult.Error(message = "error loading ticket")
//        )
//        viewModel.fetchTicket(
//            ticketUi = TicketUi(
//                colorHex = "#FFFFFF",
//                id = "123",
//                name = "test name",
//                description = "test description",
//                assignedMemberName = "test assignee member name",
//                column = ColumnUi.InProgress,
//                creationDate = LocalDateTime(
//                    year = 2025,
//                    month = 1,
//                    day = 1,
//                    hour = 1,
//                    minute = 1
//                ),
//                assignedMemberId = "test assigned member id"
//            )
//        )
//        assertEquals(1, repository.ticketCalledList.size)
//        assertEquals("123", repository.ticketCalledList[0])
//        assertEquals(2, handle.saveTicketInfoUiStateCalledList.size)
//        assertEquals(
//            TicketInfoUiState.Success(
//                Ticket(
//                    colorHex = "#FFFFFF",
//                    id = "123",
//                    name = "test name",
//                    description = "test description",
//                    assignedMemberName = "test assignee member name",
//                    column = Column.InProgress,
//                    creationDate = java.time.LocalDateTime.of(
//                        2025,
//                        1,
//                        1,
//                        1,
//                        1
//                    ),
//                    assignedMemberId = "test assigned member id"
//                )
//            ),
//            handle.saveTicketInfoUiStateCalledList[0]
//        )
//        assertEquals(
//            TicketInfoUiState.Error(message = "error loading ticket"),
//            handle.saveTicketInfoUiStateCalledList[1]
//        )
//        repository.makeExpectedTicketInfoResult(
//            TicketInfoResult.Success(
//                ticket = Ticket(
//                    colorHex = "#FFFFFF",
//                    id = "success id",
//                    name = "success name",
//                    description = "success description",
//                    assignedMemberName = "success assignee member name",
//                    column = Column.InProgress,
//                    creationDate = java.time.LocalDateTime.of(
//                        2024,
//                        2,
//                        2,
//                        2,
//                        2
//                    ),
//                    assignedMemberId = "success assigned member id"
//                )
//            )
//        )
//        assertEquals(1, repository.ticketCalledList.size)
//        assertEquals(3, handle.saveTicketInfoUiStateCalledList.size)
//        assertEquals(
//            TicketInfoUiState.Success(
//                ticket = Ticket(
//                    colorHex = "#FFFFFF",
//                    id = "success id",
//                    name = "success name",
//                    description = "success description",
//                    assignedMemberName = "success assignee member name",
//                    column = Column.InProgress,
//                    creationDate = java.time.LocalDateTime.of(
//                        2024,
//                        2,
//                        2,
//                        2,
//                        2
//                    ),
//                    assignedMemberId = "success assigned member id"
//                )
//            ),
//            handle.saveTicketInfoUiStateCalledList[2]
//        )
//        repository.makeExpectedTicketInfoResult(TicketInfoResult.NotExists)
//        assertEquals(1, repository.ticketCalledList.size)
//        assertEquals(4, handle.saveTicketInfoUiStateCalledList.size)
//        assertEquals(TicketInfoUiState.NotExists, handle.saveTicketInfoUiStateCalledList[3])
//    }
//
//    private class TestHandleTicketInfo : HandleTicketInfo {
//        private val ticketInfoUiStateMutable = MutableStateFlow<TicketInfoUiState>(
//            TicketInfoUiState.Loading
//        )
//        var ticketInfoUiStateCalledCount = 0
//        val saveTicketInfoUiStateCalledList = mutableListOf<TicketInfoUiState>()
//
//        override val ticketInfoUiState: StateFlow<TicketInfoUiState>
//            get() {
//                ticketInfoUiStateCalledCount++
//                return ticketInfoUiStateMutable
//            }
//
//        override fun saveTicketInfoUiState(ticketInfoUiState: TicketInfoUiState) {
//            saveTicketInfoUiStateCalledList.add(ticketInfoUiState)
//            ticketInfoUiStateMutable.value = ticketInfoUiState
//        }
//    }
//}