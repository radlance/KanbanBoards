package com.github.radlance.kanbanboards.profile.di

import com.github.radlance.auth.presentation.signin.CredentialResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.data.HandleProfileRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.ProfileRemoteDataSource
import com.github.radlance.kanbanboards.profile.data.RemoteProfileRepository
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import com.github.radlance.kanbanboards.profile.presentation.DeleteProfileMapper
import com.github.radlance.kanbanboards.profile.presentation.DeleteProfileUiState
import com.github.radlance.kanbanboards.profile.presentation.HandleProfile
import com.github.radlance.kanbanboards.profile.presentation.LoadProfileResultMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileCredentialMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileCredentialUiState
import com.github.radlance.kanbanboards.profile.presentation.ProfileMapperFacade
import com.github.radlance.kanbanboards.profile.presentation.ProfileProviderMapper
import com.github.radlance.kanbanboards.profile.presentation.ProfileProviderUi
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

    @Binds
    fun provideProfileProviderMapper(profileProviderMapper: ProfileProviderMapper): com.github.radlance.api.service.ProfileProvider.Mapper<ProfileProviderUi>

    @Binds
    fun provideProfileMapperFacade(profileMapperFacade: ProfileMapperFacade.Base): ProfileMapperFacade

    @Binds
    fun provideProfileCredentialMapper(profileCredentialMapper: ProfileCredentialMapper): CredentialResult.Mapper<ProfileCredentialUiState>

    @Binds
    fun provideDeleteProfileMapper(deleteProfileMapper: DeleteProfileMapper): UnitResult.Mapper<DeleteProfileUiState>

    @Binds
    fun provideHandleProfileRemoteDataSource(handleProfileRemoteDataSource: HandleProfileRemoteDataSource.Base): HandleProfileRemoteDataSource
}