package com.github.radlance.board.create.data

import com.github.radlance.board.create.domain.CreateBoardRepository
import com.github.radlance.board.create.domain.CreateBoardResult
import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.data.BoardsRemoteDataSource
import com.github.radlance.core.domain.SearchUsersResult
import com.github.radlance.core.domain.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class RemoteCreateBoardRepository @Inject constructor(
    private val boardsRemoteDataSource: BoardsRemoteDataSource,
    private val createBoardRemoteDataSource: CreateBoardRemoteDataSource,
    private val usersRepository: UsersRepository,
    private val manageResource: ManageResource
) : CreateBoardRepository {

    override suspend fun createBoard(name: String, memberIds: List<String>): CreateBoardResult {
        return if (boardsRemoteDataSource.myBoard().first().any { it.compareName(name) }) {
            CreateBoardResult.AlreadyExists(
                manageResource.string(com.github.radlance.core.R.string.board_with_this_already_exists)
            )
        } else try {
            val boardInfo = createBoardRemoteDataSource.createBoard(name, memberIds)
            CreateBoardResult.Success(boardInfo)
        } catch (e: Exception) {
            CreateBoardResult.Error(
                message = e.message ?: manageResource.string(
                    com.github.radlance.core.R.string.create_board_error
                )
            )
        }
    }

    override fun users(): Flow<SearchUsersResult> = usersRepository.users()
}