package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteBoardRepository @Inject constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardRepository {

    override fun loadBoard(): Flow<BoardInfo> {
        return boardRemoteDataSource.loadBoard()
    }
}