package com.github.radlance.kanbanboards.ticket.create.data

import com.github.radlance.kanbanboards.board.core.BaseBoardCoreTest
import com.github.radlance.kanbanboards.board.core.domain.NewTicket
import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.core.domain.User
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class CreateTicketRepositoryTest : BaseBoardCoreTest() {

    private lateinit var boardRemoteDataSource: TestBoardRemoteDataSource
    private lateinit var ticketRemoteDataSource: TestTicketRemoteDataSource
    private lateinit var manageResource: TestManageResource
    private lateinit var usersRepository: TestUsersRepository

    private lateinit var repository: CreateTicketRepository

    @Before
    fun setup() {
        boardRemoteDataSource = TestBoardRemoteDataSource()
        ticketRemoteDataSource = TestTicketRemoteDataSource()
        manageResource = TestManageResource()
        usersRepository = TestUsersRepository()

        repository = RemoteCreateTicketRepository(
            ticketRemoteDataSource = ticketRemoteDataSource,
            manageResource = manageResource,
            usersRepository = usersRepository
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
                assignedMemberIds = "new assignee id",
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
                assignedMemberIds = "new assignee id",
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
                assignedMemberIds = "new assignee id2",
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
                assignedMemberIds = "new assignee id2",
                creationDate = LocalDateTime.of(2023, 2, 2, 2, 2)
            )
        )
        assertEquals(UnitResult.Error(message = "create ticket error"), actual)
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_board_members_success() = runBlocking {
        usersRepository.makeExpectedBoardMembersResult(
            BoardMembersResult.Success(
                listOf(
                    User(
                        id = "test id",
                        email = "test@gmail.com",
                        name = "test name"
                    )
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
        assertEquals(1, usersRepository.boardMembersCalledList.size)
        assertEquals("test board id", usersRepository.boardMembersCalledList[0].first)
        assertEquals("test owner id", usersRepository.boardMembersCalledList[0].second)
    }

    @Test
    fun test_board_members_error() = runBlocking {
        usersRepository.makeExpectedBoardMembersResult(
            BoardMembersResult.Error(message = "board members error")
        )

        val actual =
            repository.boardMembers(boardId = "test board id", ownerId = "test owner id")
        assertEquals(BoardMembersResult.Error(message = "board members error"), actual.first())
        assertEquals("test board id", usersRepository.boardMembersCalledList[0].first)
        assertEquals("test owner id", usersRepository.boardMembersCalledList[0].second)
    }
}