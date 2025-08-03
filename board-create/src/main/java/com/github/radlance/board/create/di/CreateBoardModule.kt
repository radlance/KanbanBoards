package com.github.radlance.board.create.di

import com.github.radlance.board.create.data.CreateBoardRemoteDataSource
import com.github.radlance.board.create.data.RemoteCreateBoardRepository
import com.github.radlance.board.create.domain.CreateBoardRepository
import com.github.radlance.board.create.domain.CreateBoardResult
import com.github.radlance.board.create.presentation.BaseCreateBoardMapperFacade
import com.github.radlance.board.create.presentation.BaseHandleCreateBoard
import com.github.radlance.board.create.presentation.CreateBoardMapperFacade
import com.github.radlance.board.create.presentation.CreateBoardResultMapper
import com.github.radlance.board.create.presentation.CreateBoardUiState
import com.github.radlance.board.create.presentation.HandleCreateBoard
import com.github.radlance.board.create.presentation.SearchUsersResultMapper
import com.github.radlance.board.create.presentation.SearchUsersUiState
import com.github.radlance.core.domain.SearchUsersResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface CreateBoardModule {

    @Binds
    fun provideCreateBoardRepository(createBoardRepository: RemoteCreateBoardRepository): CreateBoardRepository

    @Binds
    fun provideCreateBoardResultMapper(createBoardResultMapper: CreateBoardResultMapper): CreateBoardResult.Mapper<CreateBoardUiState>

    @Binds
    fun provideCreateBoardsClodDataSource(createBoardRemoteDataSource: CreateBoardRemoteDataSource.Base): CreateBoardRemoteDataSource

    @Binds
    fun provideHandleCreateBoard(handleCreateBoard: BaseHandleCreateBoard): HandleCreateBoard

    @Binds
    fun provideSearchUsersMapper(searchUsersResultMapper: SearchUsersResultMapper): SearchUsersResult.Mapper<SearchUsersUiState>

    @Binds
    fun provideCreateBoardMapperFacade(createBoardMapperFacade: BaseCreateBoardMapperFacade): CreateBoardMapperFacade
}