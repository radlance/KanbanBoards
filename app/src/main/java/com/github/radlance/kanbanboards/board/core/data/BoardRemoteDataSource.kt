package com.github.radlance.kanbanboards.board.core.data

import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.data.IgnoreHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface BoardRemoteDataSource {

    fun board(boardId: String): Flow<BoardInfo?>

    suspend fun leaveBoard(boardId: String)

    suspend fun deleteBoard(boardId: String)

    class Base @Inject constructor(
        private val service: Service,
        private val ignoreHandle: IgnoreHandle,
        private val myUser: MyUser
    ) : BoardRemoteDataSource {

        override fun board(boardId: String): Flow<BoardInfo?> {
            val myUserId = myUser.id

            return combine(
                service.get(
                    path = "boards",
                    subPath = boardId
                ),
                service.getListByQuery(
                    path = "boards-members",
                    queryKey = "boardId",
                    queryValue = boardId
                )
            ) { boardSnapshot, membersSnapshot ->
                val key = boardSnapshot.id
                val entity = boardSnapshot.getValue(BoardEntity::class.java) ?: return@combine null

                val isMember = membersSnapshot.any {
                    it.getValue(BoardMemberEntity::class.java)?.memberId == myUserId
                }

                if (entity.owner != myUserId && !isMember) {
                    return@combine null
                }

                BoardInfo(
                    id = key,
                    name = entity.name,
                    isMyBoard = myUserId == entity.owner,
                    owner = entity.owner
                )
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override suspend fun leaveBoard(boardId: String) = ignoreHandle.handleSuspend {
            val myUserId = myUser.id
            val boardMemberSnapshot = service.getListByQueryAwait(
                path = "boards-members",
                queryKey = "boardId",
                queryValue = boardId
            )

            val result = boardMemberSnapshot.firstOrNull {
                it.child("memberId").getValue(String::class.java) == myUserId
                }

            result?.ref?.removeValue()
        }

        override suspend fun deleteBoard(boardId: String) {

            service.delete(
                path = "boards",
                itemId = boardId
            )

            val membersSnapshot = service.getSingleQueryAwait(
                path = "boards-members",
                queryKey = "boardId",
                queryValue = boardId
            )

            membersSnapshot.children.forEach { memberSnapshot ->
                memberSnapshot.ref.removeValue()
            }

            val ticketsSnapshot = service.getSingleQueryAwait(
                "tickets",
                "boardId",
                boardId
            )

            ticketsSnapshot.children.forEach { ticketSnapshot ->
                ticketSnapshot.ref.removeValue()
            }
        }
    }
}