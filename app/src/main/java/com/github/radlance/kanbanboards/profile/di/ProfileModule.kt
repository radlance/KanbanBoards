package com.github.radlance.kanbanboards.profile.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.profile.data.ProfileRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.RemoteProfileRepository
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import com.github.radlance.kanbanboards.profile.presentation.LoadProfileResultMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileUiState
import com.github.radlance.kanbanboards.profile.presentation.HandleProfile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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
}

@Module
@InstallIn(ViewModelComponent::class)
class ProfileViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideHandleProfile(
        savedStateHandle: SavedStateHandle
    ): HandleProfile {
        return HandleProfile.Base(savedStateHandle)
    }
}