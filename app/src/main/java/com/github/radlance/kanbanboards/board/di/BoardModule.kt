package com.github.radlance.kanbanboards.board.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.RemoteBoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.BoardStorageMapper
import com.github.radlance.kanbanboards.board.presentation.BoardResultMapper
import com.github.radlance.kanbanboards.board.presentation.BoardUiState
import com.github.radlance.kanbanboards.board.presentation.HandleBoard
import com.github.radlance.kanbanboards.boards.domain.Board
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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

    @Binds
    fun provideBoardResultMapper(boardResultMapper: BoardResultMapper): BoardResult.Mapper<BoardUiState>
}

@Module
@InstallIn(ViewModelComponent::class)
class BoardViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideHandleBoard(savedStateHandle: SavedStateHandle): HandleBoard {
        return HandleBoard.Base(savedStateHandle)
    }
}