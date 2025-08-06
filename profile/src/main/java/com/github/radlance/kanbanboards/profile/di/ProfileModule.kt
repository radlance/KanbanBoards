package com.github.radlance.kanbanboards.profile.di

import com.github.radlance.kanbanboards.profile.data.ProfileRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.RemoteProfileRepository
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import com.github.radlance.kanbanboards.profile.presentation.BaseProfileMapperFacade
import com.github.radlance.kanbanboards.profile.presentation.LoadProfileResultMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileMapperFacade
import com.github.radlance.kanbanboards.profile.presentation.ProfileUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ProfileModule {

    @Binds
    fun provideLoadProfileResultMapper(
        profileResultMapper: LoadProfileResultMapper
    ): LoadProfileResult.Mapper<ProfileUiState>

    @Binds
    fun provideProfileRepository(profileRepository: RemoteProfileRepository): ProfileRepository

    @Binds
    fun provideProfileRemoteDataSource(profileRemoteDataSource: ProfileRemoteDataSource.Base): ProfileRemoteDataSource

    @Binds
    fun provideProfileMapperFacade(profileMapperFacade: BaseProfileMapperFacade): ProfileMapperFacade
}