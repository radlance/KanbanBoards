package com.github.radlance.kanbanboards.createboard.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteCreateBoardRepository @Inject constructor(
    private val boardsRemoteDataSource: BoardsRemoteDataSource,
    private val createBoardRemoteDataSource: CreateBoardRemoteDataSource,
    private val manageResource: ManageResource
) : CreateBoardRepository {

    override suspend fun createBoard(name: String): CreateBoardResult {
        return if (boardsRemoteDataSource.myBoard().first().any { it.compareName(name) }) {
            CreateBoardResult.AlreadyExists(
                manageResource.string(R.string.board_with_this_already_exists)
            )
        } else try {
            val boardInfo = createBoardRemoteDataSource.createBoard(name)
            CreateBoardResult.Success(boardInfo)
        } catch (e: Exception) {
            CreateBoardResult.Error(
                message = e.message ?: manageResource.string(R.string.create_board_error)
            )
        }
    }
}