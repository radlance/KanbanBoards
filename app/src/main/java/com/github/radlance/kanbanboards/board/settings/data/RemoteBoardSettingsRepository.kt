package com.github.radlance.kanbanboards.board.settings.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteBoardSettingsRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val boardRepository: BoardRepository,
    private val boardSettingsRemoteDataSource: BoardSettingsRemoteDataSource,
    private val boardsRemoteDataSource: BoardsRemoteDataSource,
    private val manageResource: ManageResource,
) : BoardSettingsRepository {

    override fun board(boardId: String): Flow<BoardResult> = boardRepository.board(boardId)

    override fun boardSettings(boardId: String): Flow<BoardSettingsResult> {
        return combine(
            usersRemoteDataSource.users(),
            boardSettingsRemoteDataSource.boardMembers(boardId)
        ) { users, members ->
            BoardSettingsResult.Success(
                users = users,
                members = members
            )
        }.catch { e -> BoardSettingsResult.Error(e.message!!) }
    }

    override suspend fun addUserToBoard(boardId: String, userId: String) {
        boardSettingsRemoteDataSource.addUserToBoard(boardId, userId)
    }

    override suspend fun deleteUserFromBoard(boardMemberId: String) {
        boardSettingsRemoteDataSource.deleteUserFromBoard(boardMemberId)
    }

    override suspend fun updateBoardName(boardInfo: BoardInfo): UpdateBoardNameResult {
        return if (boardsRemoteDataSource.myBoard().first()
                .any { it.compareName(boardInfo.name) }
        ) {
            UpdateBoardNameResult.AlreadyExists(
                manageResource.string(R.string.board_with_this_already_exists)
            )
        } else try {
            boardSettingsRemoteDataSource.updateBoardName(boardInfo)
            UpdateBoardNameResult.Success
        } catch (e: Exception) {
            UpdateBoardNameResult.Error(
                message = e.message ?: manageResource.string(R.string.create_board_error)
            )
        }
    }
}