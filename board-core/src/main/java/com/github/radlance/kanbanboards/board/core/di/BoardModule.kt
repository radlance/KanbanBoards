package com.github.radlance.kanbanboards.board.core.di

import com.github.radlance.kanbanboards.board.core.data.BaseBoardRemoteDataSource
import com.github.radlance.kanbanboards.board.core.data.BaseTicketRemoteDataSource
import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.board.core.data.ColumnTypeMapper
import com.github.radlance.kanbanboards.board.core.data.RemoteBoardRepository
import com.github.radlance.kanbanboards.board.core.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.board.core.domain.BoardRepository
import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.board.core.domain.TicketResult
import com.github.radlance.kanbanboards.board.core.presentation.BaseBoardMapperFacade
import com.github.radlance.kanbanboards.board.core.presentation.BaseHandleBoard
import com.github.radlance.kanbanboards.board.core.presentation.BoardMapperFacade
import com.github.radlance.kanbanboards.board.core.presentation.BoardResultMapper
import com.github.radlance.kanbanboards.board.core.presentation.BoardUiState
import com.github.radlance.kanbanboards.board.core.presentation.ColumnMapper
import com.github.radlance.kanbanboards.board.core.presentation.ColumnUi
import com.github.radlance.kanbanboards.board.core.presentation.ColumnUiMapper
import com.github.radlance.kanbanboards.board.core.presentation.HandleBoard
import com.github.radlance.kanbanboards.board.core.presentation.TicketBoardUiState
import com.github.radlance.kanbanboards.board.core.presentation.TicketResultMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface BoardModule {

    @Binds
    fun provideBoardDataSource(boardRemoteDataSource: BaseBoardRemoteDataSource): BoardRemoteDataSource

    @Binds
    fun provideBoardRepository(boardRepository: RemoteBoardRepository): BoardRepository

    @Binds
    fun provideBoardResultMapper(boardResultMapper: BoardResultMapper): BoardResult.Mapper<BoardUiState>

    @Binds
    fun provideTicketDataSource(ticketRemoteDataSource: BaseTicketRemoteDataSource): TicketRemoteDataSource

    @Binds
    fun provideTicketResultMapper(ticketResultMapper: TicketResultMapper): TicketResult.Mapper<TicketBoardUiState>

    @Binds
    fun provideColumnMapper(columnMapper: ColumnMapper): Column.Mapper<ColumnUi>

    @Binds
    fun provideColumnUiMapper(columnUiMapper: ColumnUiMapper): ColumnUi.Mapper<Column>

    @Binds
    fun provideBoardMapperFacade(boardMapperFacade: BaseBoardMapperFacade): BoardMapperFacade

    @Binds
    fun provideHandleBoard(handleBoard: BaseHandleBoard): HandleBoard

    @Binds
    fun provideColumnTypeMapper(columnTypeMapper: ColumnTypeMapper): Column.Mapper<String>
}