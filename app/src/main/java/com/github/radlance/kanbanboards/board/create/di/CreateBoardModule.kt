package com.github.radlance.kanbanboards.board.create.di

import com.github.radlance.kanbanboards.board.create.data.CreateBoardRemoteDataSource
import com.github.radlance.kanbanboards.board.create.data.RemoteCreateBoardRepository
import com.github.radlance.kanbanboards.board.create.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.board.create.domain.CreateBoardResult
import com.github.radlance.kanbanboards.board.create.presentation.CreateBoardMapperFacade
import com.github.radlance.kanbanboards.board.create.presentation.CreateBoardResultMapper
import com.github.radlance.kanbanboards.board.create.presentation.CreateBoardUiState
import com.github.radlance.kanbanboards.board.create.presentation.HandleCreateBoard
import com.github.radlance.kanbanboards.board.create.presentation.SearchUsersResultMapper
import com.github.radlance.kanbanboards.board.create.presentation.SearchUsersUiState
import com.github.radlance.kanbanboards.common.domain.SearchUsersResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CreateBoardModule {

    @Binds
    fun provideCreateBoardRepository(createBoardRepository: RemoteCreateBoardRepository): CreateBoardRepository

    @Binds
    fun provideCreateBoardResultMapper(createBoardResultMapper: CreateBoardResultMapper): CreateBoardResult.Mapper<CreateBoardUiState>

    @Binds
    fun provideCreateBoardsClodDataSource(createBoardRemoteDataSource: CreateBoardRemoteDataSource.Base): CreateBoardRemoteDataSource

    @Binds
    fun provideHandleCreateBoard(handleCreateBoard: HandleCreateBoard.Base): HandleCreateBoard

    @Binds
    fun provideSearchUsersMapper(searchUsersResultMapper: SearchUsersResultMapper): SearchUsersResult.Mapper<SearchUsersUiState>

    @Binds
    fun provideCreateBoardMapperFacade(createBoardMapperFacade: CreateBoardMapperFacade.Base): CreateBoardMapperFacade
}