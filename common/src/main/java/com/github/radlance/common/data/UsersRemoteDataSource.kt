package com.github.radlance.common.data

import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.common.domain.User
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
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class BaseUsersRemoteDataSource @Inject constructor(
    private val service: Service,
    private val myUser: MyUser
) : UsersRemoteDataSource {

    override fun user(userId: String): Flow<User> = service.get(
        path = "users",
        subPath = userId
    ).mapNotNull { memberSnapshot ->
        val userProfileEntity = memberSnapshot.getValue(UserProfileEntity::class.java)
        with(userProfileEntity ?: return@mapNotNull null) {
            User(id = userId, email = email, name = name ?: "")
        }
    }

    override fun users(): Flow<List<User>> {
        val currentUserId = myUser.id
        val usersQuery = service.get(path = "users")

        return usersQuery.flatMapLatest { usersSnapshot ->
            val userIds = usersSnapshot.children.map {
                it.id
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
        val membersQuery = service.getListByQuery(
            path = "boards-members",
            queryKey = "boardId",
            queryValue = boardId
        )

        return membersQuery.flatMapLatest { membersSnapshot ->
            val memberIds = buildList {
                add(ownerId)
                addAll(
                    membersSnapshot.mapNotNull {
                        it.getValue(BoardMemberEntity::class.java)?.memberId
                    }
                )
            }

            combine(
                memberIds.map { memberId -> user(memberId) }
            ) { users: Array<User> -> users.toList() }

        }.catch { e -> throw IllegalStateException(e.message) }
    }
}