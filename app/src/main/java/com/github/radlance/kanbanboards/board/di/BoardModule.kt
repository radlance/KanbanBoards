package com.github.radlance.kanbanboards.board.di

import com.github.radlance.kanbanboards.board.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.data.RemoteBoardRepository
import com.github.radlance.kanbanboards.board.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.board.domain.BoardResult
import com.github.radlance.kanbanboards.board.domain.Column
import com.github.radlance.kanbanboards.board.domain.TicketResult
import com.github.radlance.kanbanboards.board.presentation.BoardMapperFacade
import com.github.radlance.kanbanboards.board.presentation.BoardResultMapper
import com.github.radlance.kanbanboards.board.presentation.BoardUiState
import com.github.radlance.kanbanboards.board.presentation.ColumnMapper
import com.github.radlance.kanbanboards.board.presentation.ColumnUi
import com.github.radlance.kanbanboards.board.presentation.ColumnUiMapper
import com.github.radlance.kanbanboards.board.presentation.HandleBoard
import com.github.radlance.kanbanboards.board.presentation.TicketResultMapper
import com.github.radlance.kanbanboards.board.presentation.TicketUiState
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
    fun provideBoardResultMapper(boardResultMapper: BoardResultMapper): BoardResult.Mapper<BoardUiState>

    @Binds
    fun provideTicketDataSource(ticketRemoteDataSource: TicketRemoteDataSource.Base): TicketRemoteDataSource

    @Binds
    fun provideTicketResultMapper(ticketResultMapper: TicketResultMapper): TicketResult.Mapper<TicketUiState>

    @Binds
    fun provideColumnMapper(columnMapper: ColumnMapper): Column.Mapper<ColumnUi>

    @Binds
    fun provideColumnUiMapper(columnUiMapper: ColumnUiMapper): ColumnUi.Mapper<Column>

    @Binds
    fun provideBoardMapperFacade(boardMapperFacade: BoardMapperFacade.Base): BoardMapperFacade

    @Binds
    fun provideHandleBoard(handleBoard: HandleBoard.Base): HandleBoard
}