package com.github.radlance.board.settings.data

import com.github.radlance.board.R
import com.github.radlance.board.core.domain.BoardRepository
import com.github.radlance.board.core.domain.BoardResult
import com.github.radlance.board.settings.domain.BoardSettingsRepository
import com.github.radlance.board.settings.domain.BoardSettingsResult
import com.github.radlance.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.data.BoardsRemoteDataSource
import com.github.radlance.core.data.IgnoreHandle
import com.github.radlance.core.data.UsersRemoteDataSource
import com.github.radlance.core.domain.BoardInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime
import javax.inject.Inject

class RemoteBoardSettingsRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val boardRepository: BoardRepository,
    private val boardSettingsRemoteDataSource: BoardSettingsRemoteDataSource,
    private val boardsRemoteDataSource: BoardsRemoteDataSource,
    private val manageResource: ManageResource,
    private val ignoreHandle: IgnoreHandle
) : BoardSettingsRepository {

    override fun board(boardId: String): Flow<BoardResult> = boardRepository.board(boardId)

    override fun boardSettings(boardId: String): Flow<BoardSettingsResult> {
        return combine(
            usersRemoteDataSource.users(),
            boardSettingsRemoteDataSource.boardMembers(boardId),
            boardSettingsRemoteDataSource.invitedUsers(boardId)
        ) { users, members, invited ->
            BoardSettingsResult.Success(
                users = users,
                members = members,
                invited = invited
            )
        }.catch { e -> BoardSettingsResult.Error(e.message!!) }
    }

    override fun inviteUserToBoard(
        boardId: String,
        userId: String,
        sendDate: ZonedDateTime
    ) = ignoreHandle.handle {
        boardSettingsRemoteDataSource.inviteUserToBoard(boardId, userId, sendDate)
    }

    override fun deleteUserFromBoard(boardMemberId: String) = ignoreHandle.handle {
        boardSettingsRemoteDataSource.deleteUserFromBoard(boardMemberId)
    }

    override fun rollbackInvitation(invitedMemberId: String) = ignoreHandle.handle {
        boardSettingsRemoteDataSource.rollbackInvitation(invitedMemberId)
    }

    override suspend fun updateBoardName(boardInfo: BoardInfo): UpdateBoardNameResult {
        return if (boardsRemoteDataSource.myBoard().first()
                .any { it.compareName(boardInfo.name) }
        ) {
            UpdateBoardNameResult.AlreadyExists(
                manageResource.string(
                    com.github.radlance.core.R.string.board_with_this_already_exists
                )
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

    override suspend fun deleteBoard(boardId: String) {
        boardRepository.deleteBoard(boardId)
    }
}