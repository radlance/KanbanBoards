package com.github.radlance.kanbanboards.createboard.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.createboard.data.CreateBoardRemoteDataSource
import com.github.radlance.kanbanboards.createboard.data.RemoteCreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardRepository
import com.github.radlance.kanbanboards.createboard.domain.CreateBoardResult
import com.github.radlance.kanbanboards.createboard.presentation.CreateBoardResultMapper
import com.github.radlance.kanbanboards.createboard.presentation.CreateBoardUiState
import com.github.radlance.kanbanboards.createboard.presentation.HandleCreateBoard
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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
}

@Module
@InstallIn(ViewModelComponent::class)
class CreateBoardViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideHandleCreateBoard(savedStateHandle: SavedStateHandle): HandleCreateBoard {
        return HandleCreateBoard.Base(savedStateHandle)
    }
}
