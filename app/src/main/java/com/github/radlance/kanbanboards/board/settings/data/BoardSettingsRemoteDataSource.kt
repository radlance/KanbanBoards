package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.settings.domain.BoardUser
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.invitation.data.InvitationEntity
import com.github.radlance.kanbanboards.service.Service
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import java.time.ZonedDateTime
import javax.inject.Inject

interface BoardSettingsRemoteDataSource {

    fun inviteUserToBoard(boardId: String, userId: String, sendDate: ZonedDateTime)

    fun deleteUserFromBoard(boardMemberId: String)

    fun rollbackInvitation(invitedMemberId: String)

    fun boardMembers(boardId: String): Flow<List<BoardUser>>

    fun invitedUsers(boardId: String): Flow<List<BoardUser>>

    fun updateBoardName(boardInfo: BoardInfo)

    @OptIn(ExperimentalCoroutinesApi::class)
    class Base @Inject constructor(
        private val service: Service,
        private val handleError: HandleError
    ) : BoardSettingsRemoteDataSource {

        override fun inviteUserToBoard(
            boardId: String,
            userId: String,
            sendDate: ZonedDateTime
        ) {
            service.post(
                path = "boards-invitations",
                obj = InvitationEntity(
                    memberId = userId,
                    boardId = boardId,
                    sendDate = sendDate.toString()
                )
            )
        }

        override fun deleteUserFromBoard(boardMemberId: String) {
            service.delete(
                path = "boards-members",
                itemId = boardMemberId
            )
        }

        override fun rollbackInvitation(invitedMemberId: String) {
            service.delete(
                path = "boards-invitations",
                itemId = invitedMemberId
            )
        }

        override fun boardMembers(boardId: String): Flow<List<BoardUser>> = boardUsers(
            boardId = boardId,
            child = "boards-members"
        )

        override fun invitedUsers(boardId: String): Flow<List<BoardUser>> = boardUsers(
            boardId = boardId,
            child = "boards-invitations"
        )

        override fun updateBoardName(boardInfo: BoardInfo) {
            try {
                service.update(
                    path = "boards",
                    subPath = boardInfo.id,
                    obj = boardInfo
                )
            } catch (e: Exception) {
                handleError.handle(e)
            }
        }

        private fun boardUsers(boardId: String, child: String): Flow<List<BoardUser>> {
            val membersQuery = service.getListByQuery(
                path = child,
                queryKey = "boardId",
                queryValue = boardId
            )

            return membersQuery.flatMapLatest { snapshots ->
                val ids: List<Pair<String, String>> = snapshots.map {
                    Pair(it.id, it.getValue(BoardMemberEntity::class.java)!!.memberId)
                }

                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        ids.map { pair ->
                            service.get(
                                path = "users",
                                subPath = pair.second
                            ).mapNotNull { memberSnapshot ->
                                val userProfileEntity =
                                    memberSnapshot.getValue(UserProfileEntity::class.java)
                                with(userProfileEntity ?: return@mapNotNull null) {
                                    BoardUser(
                                        id = pair.first,
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