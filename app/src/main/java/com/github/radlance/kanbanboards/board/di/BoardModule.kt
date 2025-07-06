package com.github.radlance.kanbanboards.board.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.RemoteBoardRepository
import com.github.radlance.kanbanboards.board.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.TicketResult
import com.github.radlance.kanbanboards.board.presentation.BoardResultMapper
import com.github.radlance.kanbanboards.board.presentation.BoardUiState
import com.github.radlance.kanbanboards.board.presentation.ColumnMapper
import com.github.radlance.kanbanboards.board.presentation.ColumnUi
import com.github.radlance.kanbanboards.board.presentation.HandleBoard
import com.github.radlance.kanbanboards.board.presentation.TicketResultMapper
import com.github.radlance.kanbanboards.board.presentation.TicketUiState
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
    fun provideBoardResultMapper(boardResultMapper: BoardResultMapper): BoardResult.Mapper<BoardUiState>

    @Binds
    fun provideTicketDataSource(ticketRemoteDataSource: TicketRemoteDataSource.Base): TicketRemoteDataSource

    @Binds
    fun provideTicketResultMapper(ticketResultMapper: TicketResultMapper): TicketResult.Mapper<TicketUiState>

    @Binds
    fun provideColumnMapper(columnMapper: ColumnMapper): Column.Mapper<ColumnUi>
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