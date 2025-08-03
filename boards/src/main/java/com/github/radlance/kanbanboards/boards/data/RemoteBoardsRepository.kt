package com.github.radlance.kanbanboards.boards.data

import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.domain.BoardsResult
import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.core.domain.Board
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class RemoteBoardsRepository @Inject constructor(
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
            emit(
                BoardsResult.Error(
                    e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
                )
            )
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