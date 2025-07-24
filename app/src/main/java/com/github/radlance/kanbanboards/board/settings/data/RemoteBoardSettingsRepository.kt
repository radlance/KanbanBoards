package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RemoteBoardSettingsRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val boardRepository: BoardRepository
) : BoardSettingsRepository {

    override fun board(boardId: String): Flow<BoardResult> = boardRepository.board(boardId)

    override fun boardSettings(boardId: String, ownerId: String): Flow<BoardSettingsResult> {
        return combine(
            usersRemoteDataSource.users(),
            usersRemoteDataSource.boardMembers(boardId, ownerId)
        ) { users, members ->
            BoardSettingsResult.Success(
                users = users,
                members = members
            )
        }.catch { e -> BoardSettingsResult.Error(e.message!!) }
    }
}