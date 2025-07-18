package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMember
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface BoardRemoteDataSource {

    fun loadBoard(boardId: String): Flow<BoardInfo>

    fun boardMembers(boardId: String): Flow<List<BoardMember>>

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : BoardRemoteDataSource {

        override fun loadBoard(boardId: String): Flow<BoardInfo> {
            val myUserId = Firebase.auth.currentUser!!.uid

            return provideDatabase.database()
                .child("boards")
                .child(boardId).snapshots.mapNotNull {
                    val key = it.key ?: return@mapNotNull null
                    val entity = it.getValue<BoardEntity>() ?: return@mapNotNull null
                    BoardInfo(id = key, name = entity.name, isMyBoard = myUserId == entity.owner)
                }.catch { e -> throw IllegalStateException(e.message) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun boardMembers(boardId: String): Flow<List<BoardMember>> {
            val membersQuery = provideDatabase.database()
                .child("boards-members")
                .orderByChild("boardId")
                .equalTo(boardId)

            return membersQuery.snapshots.flatMapLatest { membersSnapshot ->
                val memberIds = membersSnapshot.children.mapNotNull {
                    it.getValue<BoardMemberEntity>()?.memberId
                }

                if (memberIds.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        memberIds.map { memberId -> boardMember(memberId) }
                    ) { boards: Array<BoardMember> -> boards.toList() }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        private fun boardMember(memberId: String): Flow<BoardMember> = provideDatabase
            .database()
            .child("users")
            .child(memberId)
            .snapshots.mapNotNull { memberSnapshot ->
                val userProfileEntity = memberSnapshot.getValue<UserProfileEntity>()
                with(userProfileEntity ?: return@mapNotNull null) {
                    BoardMember(id = memberId, email = email, name = name ?: "")
                }
            }
    }
}