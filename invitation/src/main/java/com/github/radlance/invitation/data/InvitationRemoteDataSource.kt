package com.github.radlance.invitation.data

import com.github.radlance.core.data.BoardEntity
import com.github.radlance.core.data.BoardMemberEntity
import com.github.radlance.core.data.UserProfileEntity
import com.github.radlance.invitation.domain.Invitation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import java.time.ZonedDateTime
import javax.inject.Inject

interface InvitationRemoteDataSource {

    fun invitations(): Flow<List<Invitation>>

    fun accept(boardId: String, invitationId: String)

    fun decline(invitationId: String)

    class Base @Inject constructor(
        private val service: com.github.radlance.api.service.Service,
        private val myUser: com.github.radlance.api.service.MyUser
    ) : InvitationRemoteDataSource {

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun invitations(): Flow<List<Invitation>> {
            val invitationsQuery = service.getListByQuery(
                path = "boards-invitations",
                queryKey = "memberId",
                queryValue = myUser.id
            )

            return invitationsQuery.flatMapLatest { invitationsSnapshot ->
                val boardInvitations = invitationsSnapshot.mapNotNull { invitationSnapshot ->
                    invitationSnapshot.getValue(InvitationEntity::class.java)?.let { entity ->
                        entity to invitationSnapshot.id
                    }
                }

                if (boardInvitations.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        boardInvitations.map { (invitationEntity, invitationId) ->
                            service.get(
                                path = "boards",
                                subPath = invitationEntity.boardId
                            ).flatMapLatest { boardSnapshot ->
                                val boardEntity = boardSnapshot.getValue(BoardEntity::class.java)
                                boardEntity?.let {
                                    service.get(
                                        path = "users",
                                        subPath = boardEntity.owner
                                    ).mapNotNull { userSnapshot ->
                                        val user =
                                            userSnapshot.getValue(UserProfileEntity::class.java)
                                        Invitation(
                                            id = invitationId,
                                            boardId = boardSnapshot.id,
                                            boardName = boardEntity.name,
                                            sendDate = ZonedDateTime.parse(invitationEntity.sendDate),
                                            ownerEmail = user?.email ?: return@mapNotNull null
                                        )
                                    }
                                } ?: flowOf(null)
                            }.filterNotNull()
                        }
                    ) { invitations: Array<Invitation> ->
                        invitations.sortedByDescending {
                            it.sendDate
                        }.toList()
                    }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override fun accept(boardId: String, invitationId: String) {
            service.post(
                path = "boards-members",
                obj = BoardMemberEntity(
                    memberId = myUser.id,
                    boardId = boardId
                )
            )

            service.delete(
                path = "boards-invitations",
                itemId = invitationId
            )
        }

        override fun decline(invitationId: String) {
            service.delete(
                path = "boards-invitations",
                itemId = invitationId
            )
        }
    }
}