package com.github.radlance.boards.di

import com.github.radlance.boards.data.RemoteBoardsRepository
import com.github.radlance.boards.domain.BoardsRepository
import com.github.radlance.boards.domain.BoardsResult
import com.github.radlance.boards.presentation.BoardMapper
import com.github.radlance.boards.presentation.BoardUi
import com.github.radlance.boards.presentation.BoardsResultMapper
import com.github.radlance.boards.presentation.BoardsUiState
import com.github.radlance.core.data.BoardsRemoteDataSource
import com.github.radlance.core.domain.Board
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface BoardsModule {

    @Binds
    fun provideBoardsRepository(boardsRepository: RemoteBoardsRepository): BoardsRepository

    @Binds
    fun provideBoardsRemoteDataSource(boardsRemoteDataSource: BoardsRemoteDataSource.Base): BoardsRemoteDataSource

    @Binds
    fun provideBoardMapper(boardMapper: BoardMapper): Board.Mapper<BoardUi>

    @Binds
    fun provideBoardsResultMapper(boardsResultMapper: BoardsResultMapper): BoardsResult.Mapper<BoardsUiState>
}