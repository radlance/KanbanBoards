package com.github.radlance.kanbanboards.common.data

import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.common.domain.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface UsersRemoteDataSource {

    fun user(userId: String): Flow<User>

    fun users(): Flow<List<User>>

    fun boardMembers(boardId: String, ownerId: String): Flow<List<User>>

    @OptIn(ExperimentalCoroutinesApi::class)
    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : UsersRemoteDataSource {

        override fun user(userId: String): Flow<User> = provideDatabase
            .database()
            .child("users")
            .child(userId)
            .snapshots.mapNotNull { memberSnapshot ->
                val userProfileEntity = memberSnapshot.getValue<UserProfileEntity>()
                with(userProfileEntity ?: return@mapNotNull null) {
                    User(id = userId, email = email, name = name ?: "")
                }
            }

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
                        userIds.map { userId -> user(userId) }
                    ) { users -> users.toList() }
                }
            }
        }

        override fun boardMembers(boardId: String, ownerId: String): Flow<List<User>> {
            val membersQuery = provideDatabase.database()
                .child("boards-members")
                .orderByChild("boardId")
                .equalTo(boardId)

            return membersQuery.snapshots.flatMapLatest { membersSnapshot ->
                val memberIds = buildList {
                    add(ownerId)
                    addAll(
                        membersSnapshot.children.mapNotNull {
                            it.getValue<BoardMemberEntity>()?.memberId
                        }
                    )
                }

                combine(
                    memberIds.map { memberId -> user(memberId) }
                ) { users: Array<User> -> users.toList() }

            }.catch { e -> throw IllegalStateException(e.message) }
        }
    }
}