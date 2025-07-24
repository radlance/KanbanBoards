package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RemoteBoardSettingsRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardSettingsRepository {

    override fun boardSettings(boardId: String, ownerId: String): Flow<BoardSettingsResult> {
        return combine(
            usersRemoteDataSource.users(),
            usersRemoteDataSource.boardMembers(boardId, ownerId),
            boardRemoteDataSource.board(boardId)
        ) { users, members, board ->
            BoardSettingsResult.Success(
                users = users,
                members = members,
                board = board
            )
        }.catch { e -> BoardSettingsResult.Error(e.message!!) }
    }
}