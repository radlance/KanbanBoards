package com.github.radlance.kanbanboards.board.settings.di

import com.github.radlance.kanbanboards.board.settings.data.RemoteBoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsMapper
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsUiState
import com.github.radlance.kanbanboards.common.domain.SearchUsersResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardSettingsModule {

    @Binds
    fun provideBoardSettingsRepository(boardSettingsRepository: RemoteBoardSettingsRepository): BoardSettingsRepository

    @Binds
    fun provideSettingsResultMapper(boardSettingsMapper: BoardSettingsMapper): SearchUsersResult.Mapper<BoardSettingsUiState>
}