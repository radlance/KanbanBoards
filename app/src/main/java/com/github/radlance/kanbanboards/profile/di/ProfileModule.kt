package com.github.radlance.kanbanboards.profile.di

import com.github.radlance.kanbanboards.profile.data.ProfileRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.RemoteProfileRepository
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import com.github.radlance.kanbanboards.profile.presentation.HandleProfile
import com.github.radlance.kanbanboards.profile.presentation.LoadProfileResultMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProfileModule {

    @Binds
    fun provideLoadProfileResultMapper(
        profileResultMapper: LoadProfileResultMapper
    ): LoadProfileResult.Mapper<ProfileUiState>

    @Binds
    fun provideProfileRepository(profileRepository: RemoteProfileRepository): ProfileRepository

    @Binds
    fun provideProfileRemoteDataSource(profileRemoteDataSource: ProfileRemoteDataSource.Base): ProfileRemoteDataSource

    @Binds
    fun provideHandleProfile(handleProfile: HandleProfile.Base): HandleProfile
}