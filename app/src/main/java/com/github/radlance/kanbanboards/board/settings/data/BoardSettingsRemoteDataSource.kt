package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface BoardSettingsRemoteDataSource {

    suspend fun inviteUserToBoard(boardId: String, userId: String)

    suspend fun deleteUserFromBoard(boardMemberId: String)

    suspend fun rollbackInvitation(invitedMemberId: String)

    fun boardMembers(boardId: String): Flow<List<BoardUser>>

    fun invitedUsers(boardId: String): Flow<List<BoardUser>>

    suspend fun updateBoardName(boardInfo: BoardInfo)

    @OptIn(ExperimentalCoroutinesApi::class)
    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError
    ) : BoardSettingsRemoteDataSource {

        override suspend fun inviteUserToBoard(boardId: String, userId: String) {
            try {
                provideDatabase.database()
                    .child("boards-invitations").push()
                    .setValue(BoardMemberEntity(memberId = userId, boardId = boardId))
                    .await()
            } catch (_: Exception) {
            }
        }

        override suspend fun deleteUserFromBoard(boardMemberId: String) {
            try {
                provideDatabase.database()
                    .child("boards-members")
                    .child(boardMemberId)
                    .removeValue()
                    .await()
            } catch (_: Exception) {
            }
        }

        override suspend fun rollbackInvitation(invitedMemberId: String) {
            try {
                provideDatabase.database()
                    .child("boards-invitations")
                    .child(invitedMemberId)
                    .removeValue()
                    .await()
            } catch (_: Exception) {
            }
        }

        override fun boardMembers(boardId: String): Flow<List<BoardUser>> = boardUsers(
            boardId = boardId,
            child = "boards-members"
        )

        override fun invitedUsers(boardId: String): Flow<List<BoardUser>> = boardUsers(
            boardId = boardId,
            child = "boards-invitations"
        )

        override suspend fun updateBoardName(boardInfo: BoardInfo) {
            try {
                provideDatabase.database()
                    .child("boards")
                    .child(boardInfo.id)
                    .setValue(boardInfo)
                    .await()
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        private fun boardUsers(boardId: String, child: String): Flow<List<BoardUser>> {
            val membersQuery = provideDatabase.database()
                .child(child)
                .orderByChild("boardId")
                .equalTo(boardId)

            return membersQuery.snapshots.flatMapLatest { membersSnapshot ->
                val ids: List<Pair<String, String>> = membersSnapshot.children.map {
                    Pair(it.key!!, it.getValue<BoardMemberEntity>()!!.memberId)
                }

                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        ids.map { pair ->
                            provideDatabase
                                .database()
                                .child("users")
                                .child(pair.second)
                                .snapshots.mapNotNull { memberSnapshot ->
                                    val userProfileEntity =
                                        memberSnapshot.getValue<UserProfileEntity>()
                                    with(userProfileEntity ?: return@mapNotNull null) {
                                        BoardUser(
                                            boardMemberId = pair.first,
                                            userId = pair.second,
                                            email = email,
                                            name = name ?: ""
                                        )
                                    }
                                }
                        }
                    ) { users: Array<BoardUser> -> users.toList() }
                }

            }.catch { e -> throw IllegalStateException(e.message) }
        }
    }
}