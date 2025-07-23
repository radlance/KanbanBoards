package com.github.radlance.kanbanboards.board.create.data

import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import com.github.radlance.kanbanboards.common.domain.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CreateBoardRemoteDataSource {

    suspend fun createBoard(name: String, memberIds: List<String>): BoardInfo

    fun users(): Flow<List<User>>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val usersRemoteDataSource: UsersRemoteDataSource,
        private val handleError: HandleError
    ) : CreateBoardRemoteDataSource {

        override suspend fun createBoard(name: String, memberIds: List<String>): BoardInfo {
            return try {
                val myUid = Firebase.auth.currentUser!!.uid
                val boardsReference = provideDatabase.database().child("boards").push()
                boardsReference.setValue(BoardEntity(name = name, owner = myUid)).await()
                val membersReference = provideDatabase.database().child("boards-members").push()
                memberIds.forEach { memberId ->
                    membersReference.setValue(
                        BoardMemberEntity(memberId = memberId, boardId = boardsReference.key!!)
                    ).await()
                }
                BoardInfo(id = boardsReference.key!!, name = name, isMyBoard = true)
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun users(): Flow<List<User>> {
            val currentUserId = Firebase.auth.currentUser!!.uid
            val usersQuery = provideDatabase.database()
                .child("users")

            return usersQuery.snapshots.flatMapLatest { usersSnapshot ->
                val userIds = usersSnapshot.children.mapNotNull {
                    it.key
                }.filter { it != currentUserId }
                if (userIds.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        userIds.map { userId -> usersRemoteDataSource.user(userId) }
                    ) { users -> users.toList() }
                }
            }
        }
    }
}