package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.api.service.MyUser
import com.github.radlance.api.service.Service
import com.github.radlance.kanbanboards.board.core.data.BoardEntity
import com.github.radlance.kanbanboards.board.core.data.BoardMemberEntity
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
interface BoardsRemoteDataSource {

    fun myBoard(): Flow<List<Board.Storage>>

    fun otherBoards(): Flow<List<Board.Storage>>

    class Base @Inject constructor(
        private val service: Service,
        private val myUser: MyUser
    ) : BoardsRemoteDataSource {

        override fun myBoard(): Flow<List<Board.Storage>> {
            val query = service.getListByQuery(
                path = "boards",
                queryKey = "owner",
                queryValue = myUser.id
            )

            return query.map { snapshots ->
                snapshots.mapNotNull {
                    val key = it.id
                    val entity = it.getValue(BoardEntity::class.java) ?: return@mapNotNull null
                    Board.My(key, entity.name)
                }

            }.catch { e -> throw IllegalStateException(e.message) }
        }

        override fun otherBoards(): Flow<List<Board.Storage>> {
            val membersQuery = service.getListByQuery(
                path = "boards-members",
                queryKey = "memberId",
                queryValue = myUser.id
            )

            return membersQuery.flatMapLatest { membersSnapshot ->
                val boardIds = membersSnapshot.mapNotNull {
                    it.getValue(BoardMemberEntity::class.java)?.boardId
                }

                if (boardIds.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        boardIds.map { boardId -> otherBoard(boardId) }
                    ) { boards: Array<Board.Storage> -> boards.toList() }
                }
            }.catch { e -> throw IllegalStateException(e.message) }
        }

        private fun otherBoard(boardId: String): Flow<Board.Other> = service.get(
            path = "boards",
            subPath = boardId
        ).flatMapLatest { boardSnapshot ->
            val boardEntity = boardSnapshot.getValue(BoardEntity::class.java)
            boardEntity?.let {
                service.get(
                    path = "users",
                    subPath = boardEntity.owner
                ).mapNotNull { userSnapshot ->
                    val user = userSnapshot.getValue(UserProfileEntity::class.java)
                    Board.Other(
                        id = boardSnapshot.id,
                        name = boardEntity.name,
                        owner = user?.email ?: return@mapNotNull null
                    )
                }
            } ?: flowOf(null)
        }.filterNotNull()
    }
}