package com.github.radlance.kanbanboards.common

import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

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

        private var boardMembersException: Exception? = null
        val boardMembersCalledList = mutableListOf<String>()
        private val boardMembers = MutableStateFlow<List<BoardMember>>(emptyList())

        fun makeExpectedBoardMembers(boardMembers: List<BoardMember>) {
            this.boardMembers.value = boardMembers
        }

        fun makeExpectedBoardMembersException(exception: Exception) {
            boardMembersException = exception
        }

        override fun loadBoard(boardId: String): Flow<BoardInfo> = flow {
            loadBoardCalledList.add(boardId)
            boardException?.let { throw it }
            boardInfo.update { it.copy(id = boardId) }
            emitAll(boardInfo)
        }

        override fun boardMembers(boardId: String): Flow<List<BoardMember>> = flow {
            boardMembersCalledList.add(boardId)
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }
    }

    protected class TestTicketRemoteDataSource : TicketRemoteDataSource {

        private var ticketsException: Exception? = null
        val ticketsCalledList = mutableListOf<String>()
        private val tickets = MutableStateFlow(emptyList<Ticket>())
        val moveTicketCalledList = mutableListOf<Pair<String, Column>>()

        private var createTicketException: Exception? = null
        val createTicketCalledList = mutableListOf<NewTicket>()

        fun makeExpectedTickets(tickets: List<Ticket>) {
            this.tickets.value = tickets
        }

        fun makeExpectedTicketsException(exception: Exception) {
            ticketsException = exception
        }

        fun makeExpectedCreateTicketException(exception: Exception) {
            createTicketException = exception
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
    }
}