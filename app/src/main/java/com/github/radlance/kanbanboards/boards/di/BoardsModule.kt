package com.github.radlance.kanbanboards.boards.di

import com.github.radlance.kanbanboards.boards.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.boards.data.RemoteBoardsRepository
import com.github.radlance.kanbanboards.boards.domain.Board
import com.github.radlance.kanbanboards.boards.domain.BoardsRepository
import com.github.radlance.kanbanboards.boards.presentation.BoardMapper
import com.github.radlance.kanbanboards.boards.presentation.BoardUi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardsModule {

    @Binds
    fun provideBoardsRepository(boardsRepository: RemoteBoardsRepository): BoardsRepository

    @Binds
    fun provideBoardsRemoteDataSource(boardsRemoteDataSource: BoardsRemoteDataSource.Base): BoardsRemoteDataSource

    @Binds
    fun provideBoardMapper(boardMapper: BoardMapper): Board.Mapper<BoardUi>
}