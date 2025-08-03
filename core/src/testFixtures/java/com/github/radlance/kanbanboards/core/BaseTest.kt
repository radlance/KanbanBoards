package com.github.radlance.kanbanboards.core

import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.core.data.DataStoreManager
import com.github.radlance.kanbanboards.core.data.IgnoreHandle
import com.github.radlance.kanbanboards.core.data.UsersRemoteDataSource
import com.github.radlance.kanbanboards.core.domain.Board
import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.SearchUsersResult
import com.github.radlance.kanbanboards.core.domain.User
import com.github.radlance.kanbanboards.core.domain.UsersRepository
import com.github.radlance.kanbanboards.core.presentation.RunAsync
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
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

    protected class TestIgnoreHandle : IgnoreHandle {

        override fun handle(action: () -> Unit) {
            try {
                action.invoke()
            } catch (_: Exception) {
            }
        }

        override suspend fun handleSuspend(action: suspend () -> Unit) {
            try {
                action.invoke()
            } catch (_: Exception) {
            }
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

    protected class TestUsersRemoteDataSource : UsersRemoteDataSource {

        private var boardMembersException: Exception? = null
        private val boardMembers = MutableStateFlow<List<User>>(emptyList())


        var usersCalledCount = 0
        private val users = MutableStateFlow<List<User>>(emptyList())

        private var usersException: Exception? = null

        fun makeExpectedUsers(users: List<User>) {
            this.users.value = users
        }

        override fun user(userId: String): Flow<User> = emptyFlow()

        override fun users(): Flow<List<User>> = flow {
            usersCalledCount++
            usersException?.let { throw it }
            emitAll(users)
        }

        override fun boardMembers(boardId: String, ownerId: String): Flow<List<User>> = flow {
            boardMembersException?.let { throw it }
            emitAll(boardMembers)
        }
    }

    protected class TestUsersRepository : UsersRepository {

        private val searchUsersResult = MutableStateFlow<SearchUsersResult>(
            SearchUsersResult.Error("initial state")
        )

        fun makeExpectedSearchUsersResult(searchUsersResult: SearchUsersResult) {
            this.searchUsersResult.value = searchUsersResult
        }

        var usersCalledCount = 0

        private val boardMembersResult = MutableStateFlow<BoardMembersResult>(
            BoardMembersResult.Error("initial state")
        )

        fun makeExpectedBoardMembersResult(boardMembersResult: BoardMembersResult) {
            this.boardMembersResult.value = boardMembersResult
        }

        val boardMembersCalledList = mutableListOf<Pair<String, String>>()

        override fun users(): Flow<SearchUsersResult> {
            usersCalledCount++
            return searchUsersResult
        }

        override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
            boardMembersCalledList.add(Pair(boardId, ownerId))
            return boardMembersResult
        }
    }
}