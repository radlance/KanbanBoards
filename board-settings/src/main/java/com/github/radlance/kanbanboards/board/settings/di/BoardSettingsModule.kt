package com.github.radlance.kanbanboards.board.settings.di

import com.github.radlance.kanbanboards.board.core.domain.BoardResult
import com.github.radlance.kanbanboards.board.settings.data.BoardSettingsRemoteDataSource
import com.github.radlance.kanbanboards.board.settings.data.RemoteBoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsRepository
import com.github.radlance.kanbanboards.board.settings.domain.BoardSettingsResult
import com.github.radlance.kanbanboards.board.settings.domain.UpdateBoardNameResult
import com.github.radlance.kanbanboards.board.settings.presentation.BaseBoardSettingsMapperFacade
import com.github.radlance.kanbanboards.board.settings.presentation.BaseHandleBoardSettings
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsMapper
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsMapperFacade
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsUiState
import com.github.radlance.kanbanboards.board.settings.presentation.HandleBoardSettings
import com.github.radlance.kanbanboards.board.settings.presentation.SettingsBoardMapper
import com.github.radlance.kanbanboards.board.settings.presentation.SettingsBoardUiState
import com.github.radlance.kanbanboards.board.settings.presentation.UpdateBoardNameMapper
import com.github.radlance.kanbanboards.board.settings.presentation.UpdateBoardNameUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface BoardSettingsModule {

    @Binds
    fun provideBoardSettingsRepository(boardSettingsRepository: RemoteBoardSettingsRepository): BoardSettingsRepository

    @Binds
    fun provideSettingsResultMapper(boardSettingsMapper: BoardSettingsMapper): BoardSettingsResult.Mapper<BoardSettingsUiState>

    @Binds
    fun provideSettingsBoardMapper(settingsBoardMapper: SettingsBoardMapper): BoardResult.Mapper<SettingsBoardUiState>

    @Binds
    fun provideBoardSettingsMapperFacade(boardSettingsMapperFacade: BaseBoardSettingsMapperFacade): BoardSettingsMapperFacade

    @Binds
    fun provideHandleBoardSettings(handleBoardSettings: BaseHandleBoardSettings): HandleBoardSettings

    @Binds
    fun provideBoardSettingsRemoteDataSource(boardSettingsRemoteDataSource: BoardSettingsRemoteDataSource.Base): BoardSettingsRemoteDataSource

    @Binds
    fun provideUpdateBoardNameMapper(updateBoardNameMapper: UpdateBoardNameMapper): UpdateBoardNameResult.Mapper<UpdateBoardNameUiState>
}