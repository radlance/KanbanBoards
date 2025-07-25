package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardMember
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

    suspend fun addUserToBoard(boardId: String, userId: String)

    suspend fun deleteUserFromBoard(boardMemberId: String)

    fun boardMembers(boardId: String): Flow<List<BoardMember>>

    suspend fun updateBoardName(boardInfo: BoardInfo)

    suspend fun deleteBoard(boardId: String)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase,
        private val handleError: HandleError
    ) : BoardSettingsRemoteDataSource {

        override suspend fun addUserToBoard(boardId: String, userId: String) {
            try {
                provideDatabase.database()
                    .child("boards-members").push()
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

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun boardMembers(boardId: String): Flow<List<BoardMember>> {
            val membersQuery = provideDatabase.database()
                .child("boards-members")
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
                                        BoardMember(
                                            boardMemberId = pair.first,
                                            userId = pair.second,
                                            email = email,
                                            name = name ?: ""
                                        )
                                    }
                                }
                        }
                    ) { users: Array<BoardMember> -> users.toList() }
                }

            }.catch { e -> throw IllegalStateException(e.message) }
        }

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

        override suspend fun deleteBoard(boardId: String) {
            try {

                provideDatabase.database()
                    .child("boards")
                    .child(boardId)
                    .removeValue()
                    .await()

                val membersSnapshot = provideDatabase.database()
                    .child("boards-members")
                    .orderByChild("boardId")
                    .equalTo(boardId).get().await()

                repeat(membersSnapshot.children.count()) {
                    membersSnapshot.ref.removeValue().await()
                }
            } catch (_: Exception) {
            }
        }
    }
}