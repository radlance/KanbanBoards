package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import com.github.radlance.kanbanboards.common.core.ManageResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RemoteBoardsRepository @Inject constructor(
    private val remoteDataSource: BoardsRemoteDataSource,
    private val manageResource: ManageResource
) : BoardsRepository {
    override fun boards(): Flow<BoardsResult> {
        return combine<List<Board>, List<Board>, BoardsResult>(
            remoteDataSource.myBoard(),
            remoteDataSource.otherBoards()
        ) { myBoards, otherBoards ->
            BoardsResult.Success(buildMergedBoardList(myBoards, otherBoards))
        }.catch { e ->
            emit(BoardsResult.Error(e.message ?: manageResource.string(R.string.error)))
        }
    }

    private fun buildMergedBoardList(
        myBoards: List<Board>,
        otherBoards: List<Board>
    ): List<Board> {
        return buildList {
            add(Board.MyOwnBoardsTitle)
            if (myBoards.isEmpty()) {
                add(Board.NoBoardsOfMyOwnHint)
            } else {
                addAll(myBoards)
            }

            add(Board.OtherBoardsTitle)
            if (otherBoards.isEmpty()) {
                add(Board.HowToBeAddedToBoardHint)
            } else {
                addAll(otherBoards)
            }
        }
    }
}