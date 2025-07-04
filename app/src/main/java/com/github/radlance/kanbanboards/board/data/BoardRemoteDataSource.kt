package com.github.radlance.kanbanboards.board.data

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.boards.domain.Board
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

interface BoardRemoteDataSource {

    fun loadBoard(): Flow<BoardInfo>

    class Base @Inject constructor(
        private val boardsRemoteDataSource: BoardsRemoteDataSource,
        private val mapper: Board.StorageMapper<BoardInfo>
    ) : BoardRemoteDataSource {

        override fun loadBoard(): Flow<BoardInfo> {
            return merge(
                boardsRemoteDataSource.myBoard(),
                boardsRemoteDataSource.otherBoards()
            ).map { boards: List<Board.Storage> ->
                boards.first().map(mapper)
            }
        }
    }
}