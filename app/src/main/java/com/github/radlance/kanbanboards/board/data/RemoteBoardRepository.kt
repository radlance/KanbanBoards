package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteBoardRepository @Inject constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardRepository {

    override fun loadBoard(boardId: String): Flow<BoardResult> {
        return boardRemoteDataSource.loadBoard(boardId).map {
            BoardResult.Success(it)
        }.catch { e -> BoardResult.Error(e.message!!) }
    }
}