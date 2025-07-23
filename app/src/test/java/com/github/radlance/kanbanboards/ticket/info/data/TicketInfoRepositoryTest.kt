package com.github.radlance.kanbanboards.ticket.info.data

import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TicketInfoRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestTicketRemoteDataSource
    private lateinit var repository: TicketInfoRepository

    @Before
    fun setup() {
        remoteDataSource = TestTicketRemoteDataSource()
        repository = RemoteTicketInfoRepository(
            ticketRemoteDataSource = remoteDataSource
        )
    }

    @Test
    fun test_ticket_success() = runBlocking {
        val ticket = repository.ticket(ticketId = "test ticket id")
        assertEquals(
            TicketInfoResult.Success(
                ticket = Ticket(
                    id = "test ticket id",
                    colorHex = "initial color",
                    name = "initial name",
                    description = "initial description",
                    assignedMemberName = "initial assignee",
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 4, 4, 4, 4),
                    assignedMemberId = "initial assigned member id"
                )
            ),
            ticket.first()
        )
        assertEquals(1, remoteDataSource.ticketCalledList.size)
        assertEquals("test ticket id", remoteDataSource.ticketCalledList[0])
    }

    @Test
    fun test_ticket_error() = runBlocking {
        remoteDataSource.makeExpectedTicketException(IllegalStateException("server exception"))
        val actual = repository.ticket(ticketId = "stub").toList()
        assertEquals(1, remoteDataSource.ticketCalledList.size)
        assertEquals("stub", remoteDataSource.ticketCalledList[0])
        assertEquals(emptyList<TicketInfoResult>(), actual)
    }

    @Test
    fun test_ticket_not_exists() = runBlocking {
        remoteDataSource.makeExpectedTicket(null)
        val actual = repository.ticket(ticketId = "0").first()
        assertEquals(TicketInfoResult.NotExists, actual)
    }
}