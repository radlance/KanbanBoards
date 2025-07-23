package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class EditTicketRepositoryTest : BaseTest() {

    private lateinit var ticketInfoRepository: TestTicketInfoRepository
    private lateinit var ticketRemoteDataSource: TestTicketRemoteDataSource
    private lateinit var manageResource: TestManageResource
    private lateinit var boardRemoteDataSource: TestBoardRemoteDataSource

    private lateinit var repository: EditTicketRepository

    @Before
    fun setup() {
        ticketInfoRepository = TestTicketInfoRepository()
        ticketRemoteDataSource = TestTicketRemoteDataSource()
        manageResource = TestManageResource()
        boardRemoteDataSource = TestBoardRemoteDataSource()

        repository = RemoteEditTicketRepository(
            ticketInfoRepository = ticketInfoRepository,
            ticketRemoteDataSource = ticketRemoteDataSource,
            handleUnitResult = HandleUnitResult.Base(manageResource),
            boardRemoteDataSource = boardRemoteDataSource
        )
    }

    @Test
    fun test_edit_ticket_success() = runBlocking {
        val actual = repository.editTicket(
            EditTicket(
                boardId = "edited boardId",
                colorHex = "#111111",
                name = "v name",
                description = "edited description",
                assignedMemberId = "edited assignee id",
                creationDate = LocalDateTime.of(2024, 2, 2, 2, 2),
                id = "edited id",
                column = Column.Done
            )
        )

        assertEquals(UnitResult.Success, actual)
        assertEquals(1, ticketRemoteDataSource.editTicketCalledList.size)
        assertEquals(
            EditTicket(
                boardId = "edited boardId",
                colorHex = "#111111",
                name = "v name",
                description = "edited description",
                assignedMemberId = "edited assignee id",
                creationDate = LocalDateTime.of(2024, 2, 2, 2, 2),
                id = "edited id",
                column = Column.Done
            ),
            ticketRemoteDataSource.editTicketCalledList[0]
        )
    }

    @Test
    fun test_edit_ticket_error_with_message() = runBlocking {
        ticketRemoteDataSource.makeExpectedEditTicketException(
            IllegalStateException("some error")
        )
        val actual = repository.editTicket(
            EditTicket(
                boardId = "edited boardId2",
                colorHex = "#111111",
                name = "edited name2",
                description = "edited description2",
                assignedMemberId = "edited assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2),
                id = "edited id2",
                column = Column.Todo
            )
        )
        assertEquals(UnitResult.Error(message = "some error"), actual)
        assertEquals(1, ticketRemoteDataSource.editTicketCalledList.size)
        assertEquals(
            EditTicket(
                boardId = "edited boardId2",
                colorHex = "#111111",
                name = "edited name2",
                description = "edited description2",
                assignedMemberId = "edited assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2),
                id = "edited id2",
                column = Column.Todo
            ),
            ticketRemoteDataSource.editTicketCalledList[0]
        )
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_edit_ticket_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "create ticket error")
        ticketRemoteDataSource.makeExpectedEditTicketException(
            IllegalStateException()
        )
        val actual = repository.editTicket(
            EditTicket(
                boardId = "edited boardId2",
                colorHex = "#111111",
                name = "edited name2",
                description = "edited description2",
                assignedMemberId = "edited assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2),
                id = "edited id2",
                column = Column.Todo
            )
        )
        assertEquals(UnitResult.Error(message = "create ticket error"), actual)
        assertEquals(1, ticketRemoteDataSource.editTicketCalledList.size)
        assertEquals(
            EditTicket(
                boardId = "edited boardId2",
                colorHex = "#111111",
                name = "edited name2",
                description = "edited description2",
                assignedMemberId = "edited assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2),
                id = "edited id2",
                column = Column.Todo
            ),
            ticketRemoteDataSource.editTicketCalledList[0]
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_ticket_error_with_message() = runBlocking {
        ticketRemoteDataSource.makeExpectedDeleteTicketException(
            IllegalStateException("some error")
        )
        val actual = repository.deleteTicket(ticketId = "123")
        assertEquals(UnitResult.Error(message = "some error"), actual)
        assertEquals(1, ticketRemoteDataSource.deleteTicketCalledList.size)
        assertEquals("123", ticketRemoteDataSource.deleteTicketCalledList[0])
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_ticket_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "create ticket error")
        ticketRemoteDataSource.makeExpectedDeleteTicketException(
            IllegalStateException()
        )
        val actual = repository.deleteTicket(ticketId = "000")
        assertEquals(UnitResult.Error(message = "create ticket error"), actual)
        assertEquals(1, ticketRemoteDataSource.deleteTicketCalledList.size)
        assertEquals("000", ticketRemoteDataSource.deleteTicketCalledList[0])
        assertEquals(1, manageResource.stringCalledCount)
    }
}