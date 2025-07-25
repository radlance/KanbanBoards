package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.data.RemoteUsersRepository
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class CreateTicketRepositoryTest : BaseTest() {

    private lateinit var boardRemoteDataSource: TestBoardRemoteDataSource
    private lateinit var ticketRemoteDataSource: TestTicketRemoteDataSource
    private lateinit var manageResource: TestManageResource
    private lateinit var usersRemoteDataSource: TestUsersRemoteDataSource

    private lateinit var repository: CreateTicketRepository

    @Before
    fun setup() {
        boardRemoteDataSource = TestBoardRemoteDataSource()
        ticketRemoteDataSource = TestTicketRemoteDataSource()
        manageResource = TestManageResource()
        usersRemoteDataSource = TestUsersRemoteDataSource()

        repository = RemoteCreateTicketRepository(
            ticketRemoteDataSource = ticketRemoteDataSource,
            manageResource = manageResource,
            usersRepository = RemoteUsersRepository(usersRemoteDataSource)
        )
    }

    @Test
    fun test_create_ticket_success() = runBlocking {

        val actual = repository.createTicket(
            NewTicket(
                boardId = "new boardId",
                colorHex = "#111111",
                name = "new name",
                description = "new description",
                assignedMemberId = "new assignee id",
                creationDate = LocalDateTime.of(2024, 2, 2, 2, 2)
            )
        )

        assertEquals(UnitResult.Success, actual)
        assertEquals(1, ticketRemoteDataSource.createTicketCalledList.size)
        assertEquals(
            NewTicket(
                boardId = "new boardId",
                colorHex = "#111111",
                name = "new name",
                description = "new description",
                assignedMemberId = "new assignee id",
                creationDate = LocalDateTime.of(2024, 2, 2, 2, 2)
            ),
            ticketRemoteDataSource.createTicketCalledList[0]
        )
    }

    @Test
    fun test_create_ticket_error_with_message() = runBlocking {
        ticketRemoteDataSource.makeExpectedCreateTicketException(
            IllegalStateException("some error")
        )
        val actual = repository.createTicket(
            NewTicket(
                boardId = "new boardId2",
                colorHex = "#111111",
                name = "new name2",
                description = "new description2",
                assignedMemberId = "new assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2)
            )
        )
        assertEquals(UnitResult.Error(message = "some error"), actual)
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_create_ticket_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "create ticket error")
        ticketRemoteDataSource.makeExpectedCreateTicketException(
            IllegalStateException()
        )
        val actual = repository.createTicket(
            NewTicket(
                boardId = "new boardId2",
                colorHex = "#111111",
                name = "new name2",
                description = "new description2",
                assignedMemberId = "new assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2)
            )
        )
        assertEquals(UnitResult.Error(message = "create ticket error"), actual)
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_board_members_success() = runBlocking {
        usersRemoteDataSource.makeExpectedBoardMembers(
            users = listOf(
                User(
                    id = "test id",
                    email = "test@gmail.com",
                    name = "test name"
                )
            )
        )
        assertEquals(
            BoardMembersResult.Success(
                listOf(
                    User(
                        id = "test id",
                        email = "test@gmail.com",
                        name = "test name"
                    )
                )
            ),
            repository.boardMembers(boardId = "test board id", ownerId = "test owner id").first()
        )
        assertEquals(1, usersRemoteDataSource.boardMembersCalledList.size)
        assertEquals("test board id", usersRemoteDataSource.boardMembersCalledList[0].first)
        assertEquals("test owner id", usersRemoteDataSource.boardMembersCalledList[0].second)
    }

    @Test
    fun test_board_members_error() = runBlocking {
        usersRemoteDataSource.makeExpectedBoardMembersException(
            IllegalStateException("some another error")
        )

        val actual = repository.boardMembers(boardId = "test board id", ownerId = "test owner id").toList()
        assertEquals(0, actual.size)
    }
}