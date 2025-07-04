package com.github.radlance.kanbanboards.board.di

import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.RemoteBoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardStorageMapper
import com.github.radlance.kanbanboards.boards.domain.Board
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardModule {

    @Binds
    fun provideBoardDataSource(boardRemoteDataSource: BoardRemoteDataSource.Base): BoardRemoteDataSource

    @Binds
    fun provideBoardRepository(boardRepository: RemoteBoardRepository): BoardRepository

    @Binds
    fun provideStorageMapper(storageMapper: BoardStorageMapper): Board.StorageMapper<BoardInfo>
}