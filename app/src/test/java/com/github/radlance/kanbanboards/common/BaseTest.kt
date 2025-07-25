package com.github.radlance.kanbanboards.common

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.domain.User
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.common.presentation.HandleTicket
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

abstract class BaseTest {
    protected class TestRunAsync : RunAsync {
        override fun <T : Any> async(
            background: suspend () -> T,
            ui: (T) -> Unit,
            scope: CoroutineScope
        ) = runBlocking {
            val result = background.invoke()
            ui.invoke(result)
        }

        override fun <T> stateInAsync(
            flow: Flow<T>,
            started: SharingStarted,
            initialValue: T,
            scope: CoroutineScope
        ): StateFlow<T> = flow.stateIn(
            CoroutineScope(Dispatchers.Unconfined),
            SharingStarted.Eagerly,
            initialValue
        )

        override fun <T> launchInAsync(flow: Flow<T>, coroutineScope: CoroutineScope): Job {
            return flow.launchIn(CoroutineScope(Dispatchers.Unconfined))
        }
    }

    protected class TestManageResource : ManageResource {

        var stringCalledCount = 0

        private var string = ""

        fun makeExpectedString(expected: String) {
            string = expected
        }

        override fun string(id: Int): String {
            stringCalledCount++
            return string
        }
    }

    protected class TestDataStoreManager : DataStoreManager {

        private val authorizedCurrent = MutableStateFlow(false)
        val saveAuthorizedCalledList = mutableListOf<Boolean>()
        var authorizedCalledCount = 0

        override suspend fun saveAuthorized(authorized: Boolean) {
            saveAuthorizedCalledList.add(authorized)
            authorizedCurrent.value = authorized
        }

        override fun authorized(): Flow<Boolean> {
            authorizedCalledCount++
            return authorizedCurrent
        }
    }

    protected class TestBoardsRemoteDataSource : BoardsRemoteDataSource {

        var otherBoardsCalledCount = 0
        private val otherBoards = MutableStateFlow<List<Board.Storage>>(emptyList())
        private var anyBoardsException: Exception? = null
        var boardsCalledCount = 0
        private val boards = MutableStateFlow<List<Board.Storage>>(emptyList())

        fun makeExpectedMyBoards(myBoards: List<Board.Storage>) {
            this.boards.value = myBoards
        }

        fun makeExpectedOtherBoards(otherBoards: List<Board.Storage>) {
            this.otherBoards.value = otherBoards
        }

        fun makeExpectedBoardsException(expected: Exception) {
            anyBoardsException = expected
        }

        override fun myBoard(): Flow<List<Board.Storage>> = flow {
            boardsCalledCount++
            anyBoardsException?.let { throw it }
            emitAll(boards)
        }

        override fun otherBoards(): Flow<List<Board.Storage>> = flow {
            otherBoardsCalledCount++
            anyBoardsException?.let { throw it }
            emitAll(otherBoards)
        }
    }

    protected class TestBoardRemoteDataSource : BoardRemoteDataSource {

        private var boardException: Exception? = null
        val loadBoardCalledList = mutableListOf<String>()
        private val boardInfo = MutableStateFlow(
            BoardInfo(
                id = "default id",
                name = "default name",
                isMyBoard = true,
                owner = "default owner"
            )
        )

        fun makeExpectedBoardException(exception: Exception) {
            boardException = exception
        }


        override fun board(boardId: String): Flow<BoardInfo> = flow {
            loadBoardCalledList.add(boardId)
            boardException?.let { throw it }
            boardInfo.update { it.copy(id = boardId) }
            emitAll(boardInfo)
        }
    }

    protected class TestTicketRemoteDataSource : TicketRemoteDataSource {

        private var ticketsException: Exception? = null
        val ticketsCalledList = mutableListOf<String>()
        private val tickets = MutableStateFlow(emptyList<Ticket>())
        val moveTicketCalledList = mutableListOf<Pair<String, Column>>()

        private var createTicketException: Exception? = null
        val createTicketCalledList = mutableListOf<NewTicket>()

        private val ticket = MutableStateFlow<Ticket?>(
            Ticket(
                id = "initial id",
                colorHex = "initial color",
                name = "initial name",
                description = "initial description",
                assignedMemberName = "initial assignee",
                column = Column.Todo,
                creationDate = LocalDateTime.of(2025, 4, 4, 4, 4),
                assignedMemberId = "initial assigned member id"
            )
        )
        private var ticketException: Exception? = null
        val ticketCalledList = mutableListOf<String>()

        private var editTicketException: Exception? = null
        val editTicketCalledList = mutableListOf<EditTicket>()

        private var deleteTicketException: Exception? = null
        val deleteTicketCalledList = mutableListOf<String>()

        fun makeExpectedTickets(tickets: List<Ticket>) {
            this.tickets.value = tickets
        }

        fun makeExpectedTicket(ticket: Ticket?) {
            this.ticket.value = ticket
        }

        fun makeExpectedTicketsException(exception: Exception) {
            ticketsException = exception
        }

        fun makeExpectedCreateTicketException(exception: Exception) {
            createTicketException = exception
        }

        fun makeExpectedTicketException(exception: Exception) {
            ticketException = exception
        }

        fun makeExpectedEditTicketException(exception: Exception) {
            editTicketException = exception
        }

        fun makeExpectedDeleteTicketException(exception: Exception) {
            deleteTicketException = exception
        }

        override fun ticket(ticketId: String): Flow<Ticket?> = flow {
            ticketCalledList.add(ticketId)
            ticketException?.let { throw it }
            emitAll(
                if (ticket.value == null) {
                    flowOf(null)
                } else {
                    ticket.map { it!!.copy(id = ticketId) }
                }
            )
        }

        override fun tickets(boardId: String): Flow<List<Ticket>> = flow {
            ticketsCalledList.add(boardId)
            ticketsException?.let { throw it }
            emitAll(tickets.map { list -> list.map { it.copy(id = boardId) } })
        }

        override fun moveTicket(ticketId: String, column: Column) {
            moveTicketCalledList.add(Pair(ticketId, column))
        }

        override suspend fun createTicket(newTicket: NewTicket) {
            createTicketCalledList.add(newTicket)
            createTicketException?.let { throw it }
        }

        override suspend fun editTicket(ticket: EditTicket) {
            editTicketCalledList.add(ticket)
            editTicketException?.let { throw it }
        }

        override suspend fun deleteTicket(ticketId: String) {
            deleteTicketCalledList.add(ticketId)
            deleteTicketException?.let { throw it }
        }
    }

    protected class TestTicketRepository : CreateTicketRepository {

        val boardMembersCalledList = mutableListOf<Pair<String, String>>()
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

        override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
            boardMembersCalledList.add(Pair(boardId, ownerId))
            return boardMembersResult
        }

        override suspend fun createTicket(newTicket: NewTicket): UnitResult {
            createTicketCalledList.add(newTicket)
            return createTicketResult
        }
    }

    protected class TestTicketInfoRepository : TicketInfoRepository {

        private val ticketInfoResult = MutableStateFlow<TicketInfoResult>(
            TicketInfoResult.Success(
                Ticket(
                    id = "initial id",
                    colorHex = "initial color",
                    name = "initial name",
                    description = "initial description",
                    assignedMemberName = "initial assignee member name",
                    column = Column.Todo,
                    creationDate = LocalDateTime.of(2025, 1, 1, 1, 1),
                    assignedMemberId = "initial assigned member id"
                )
            )
        )

        val ticketCalledList = mutableListOf<String>()

        fun makeExpectedTicketInfoResult(ticketInfoResult: TicketInfoResult) {
            this.ticketInfoResult.value = ticketInfoResult
        }

        override fun ticket(ticketId: String): Flow<TicketInfoResult> {
            ticketCalledList.add(ticketId)
            return ticketInfoResult
        }
    }

    protected class TestBaseHandleTicket : HandleTicket {
        var ticketUiStateCalledCount = 0
        val saveCreateTicketUiStateCalledList = mutableListOf<TicketUiState>()

        private val createTicketUiStateMutable =
            MutableStateFlow<TicketUiState>(TicketUiState.Initial)

        override val ticketUiState: StateFlow<TicketUiState>
            get() {
                ticketUiStateCalledCount++
                return createTicketUiStateMutable
            }

        override fun saveTicketUiState(ticketUiState: TicketUiState) {
            saveCreateTicketUiStateCalledList.add(ticketUiState)
            createTicketUiStateMutable.value = ticketUiState
        }
    }

    protected class TestUsersRemoteDataSource : UsersRemoteDataSource {

        private var boardMembersException: Exception? = null
        val boardMembersCalledList = mutableListOf<Pair<String, String>>()
        private val boardMembers = MutableStateFlow<List<User>>(emptyList())

        fun makeExpectedBoardMembers(users: List<User>) {
            this.boardMembers.value = users
        }

        var usersCalledCount = 0
        private val users = MutableStateFlow<List<User>>(emptyList())

        private var usersException: Exception? = null

        fun makeExpectedUsers(users: List<User>) {
            this.users.value = users
        }

        fun makeExpectedUsersException(exception: Exception) {
            usersException = exception
        }

        fun makeExpectedBoardMembersException(exception: Exception) {
            boardMembersException = exception
        }

        override fun user(userId: String): Flow<User> = emptyFlow()

        override fun users(): Flow<List<User>> = flow {
            usersCalledCount++
            usersException?.let { throw it }
            emitAll(users)
        }

        override fun boardMembers(boardId: String, ownerId: String): Flow<List<User>> = flow {
            boardMembersCalledList.add(Pair(boardId, ownerId))
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }
    }

    protected class TestBoardRepository : BoardRepository {
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
}