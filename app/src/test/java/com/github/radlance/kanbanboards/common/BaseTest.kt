package com.github.radlance.kanbanboards.common

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.createboard.data.CreateBoardRemoteDataSource
import com.github.radlance.kanbanboards.login.data.AuthRemoteDataSource
import com.github.radlance.kanbanboards.navigation.data.NavigationRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.ProfileRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
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

        override fun <T> stateInViewModel(
            flow: Flow<T>,
            started: SharingStarted,
            initialValue: T,
            scope: CoroutineScope
        ): StateFlow<T> = flow.stateIn(
            CoroutineScope(Dispatchers.Unconfined),
            SharingStarted.Eagerly,
            initialValue
        )
    }

    protected class TestManageResource : ManageResource {

        var stringCalledCount = 0

        private var string = ""

        fun makeExpectedAnswer(expected: String) {
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

    protected class TestRemoteDataSource : AuthRemoteDataSource,
        NavigationRemoteDataSource,
        ProfileRemoteDataSource,
        BoardsRemoteDataSource,
        CreateBoardRemoteDataSource {

        private var userProfileEntity: UserProfileEntity? = null

        val signInCalledList = mutableListOf<String>()
        var signInException: Exception? = null

        var userExistsCalledCount = 0
        var userExists = false

        var profileCalledCount = 0
        var signOutCalledCount = 0

        var boardsCalledCount = 0
        private val boards = MutableStateFlow<List<Board.Storage>>(emptyList())

        var otherBoardsCalledCount = 0
        private val otherBoards = MutableStateFlow<List<Board.Storage>>(emptyList())

        private var anyBoardsException: Exception? = null

        var createBoardCalledCount = 0

        private var createBoardException: Exception? = null

        fun setUserData(name: String, email: String) {
            userProfileEntity = UserProfileEntity(email, name)
        }

        fun makeExpectedMyBoards(myBoards: List<Board.Storage>) {
            this.boards.value = myBoards
        }

        fun makeExpectedOtherBoards(otherBoards: List<Board.Storage>) {
            this.otherBoards.value = otherBoards
        }

        fun makeExpectedBoardsException(expected: Exception) {
            anyBoardsException = expected
        }

        fun makeExpectedCreateBoardException(expected: Exception) {
            createBoardException = expected
        }

        override suspend fun signIn(userTokenId: String) {
            signInCalledList.add(userTokenId)
            signInException?.let { throw it }
        }

        override fun userExists(): Boolean {
            userExistsCalledCount++
            return userExists
        }

        override fun profile(): UserProfileEntity {
            profileCalledCount++
            return userProfileEntity!!
        }

        override fun signOut() {
            signOutCalledCount++
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

        override suspend fun createBoard(name: String): BoardInfo {
            createBoardCalledCount++
            createBoardException?.let { throw it }
            return BoardInfo(
                id = "",
                name = name,
                isMyBoard = true
            )
        }
    }
}