package com.github.radlance.kanbanboards.invitation.data

import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface InvitationRemoteDataSource {

    fun invitations(): Flow<List<Invitation>>

    suspend fun acceptInvite(boardId: String, invitationId: String)

    suspend fun declineInvite(boardId: String, invitationId: String)

    class Base @Inject constructor(
        private val provideDatabase: ProvideDatabase
    ) : InvitationRemoteDataSource {

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun invitations(): Flow<List<Invitation>> {
            val myUserId = Firebase.auth.currentUser!!.uid
            val invitationsQuery = provideDatabase.database()
                .child("boards-invitations")
                .orderByChild("memberId")
                .equalTo(myUserId)

            return invitationsQuery.snapshots.flatMapLatest { invitationsSnapshot ->
                val boardInvitations = invitationsSnapshot.children.mapNotNull { invitationSnapshot ->
                    invitationSnapshot.getValue<BoardMemberEntity>()?.let { entity ->
                        entity.boardId to invitationSnapshot.key
                    }
                }

                if (boardInvitations.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        boardInvitations.map { (boardId, invitationId) ->
                            provideDatabase
                                .database()
                                .child("boards")
                                .child(boardId)
                                .snapshots.flatMapLatest { boardSnapshot ->
                                    val boardEntity = boardSnapshot.getValue<BoardEntity>()
                                    boardEntity?.let {
                                        provideDatabase.database()
                                            .child("users")
                                            .child(boardEntity.owner)
                                            .snapshots.mapNotNull { userSnapshot ->
                                                val user = userSnapshot.getValue<UserProfileEntity>()
                                                Invitation(
                                                    id = invitationId ?: return@mapNotNull null,
                                                    boardId = boardSnapshot.key ?: return@mapNotNull null,
                                                    boardName = boardEntity.name,
                                                    ownerEmail = user?.email ?: return@mapNotNull null
                                                )
                                            }
                                    } ?: flowOf(null)
                                }.filterNotNull()
                        }
                    ) { boards: Array<Invitation> -> boards.toList() }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override suspend fun acceptInvite(boardId: String, invitationId: String) {
            val currentUser = Firebase.auth.currentUser!!

            provideDatabase.database()
                .child("boards-members").push()
                .setValue(BoardMemberEntity(memberId = currentUser.uid, boardId = boardId))
                .await()

            provideDatabase.database()
                .child("boards-invitations")
                .child(invitationId)
                .removeValue()
                .await()
        }

        override suspend fun declineInvite(boardId: String, invitationId: String) {
            provideDatabase.database()
                .child("boards-invitations")
                .child(invitationId)
                .removeValue()
                .await()
        }
    }
}