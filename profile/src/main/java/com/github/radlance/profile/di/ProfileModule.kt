package com.github.radlance.profile.di

import com.github.radlance.auth.presentation.signin.CredentialResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.profile.data.HandleProfileRemoteDataSource
import com.github.radlance.profile.data.ProfileRemoteDataSource
import com.github.radlance.profile.data.RemoteProfileRepository
import com.github.radlance.profile.domain.LoadProfileResult
import com.github.radlance.profile.domain.ProfileRepository
import com.github.radlance.profile.presentation.BaseHandleProfile
import com.github.radlance.profile.presentation.BaseProfileMapperFacade
import com.github.radlance.profile.presentation.DeleteProfileMapper
import com.github.radlance.profile.presentation.DeleteProfileUiState
import com.github.radlance.profile.presentation.HandleProfile
import com.github.radlance.profile.presentation.LoadProfileResultMapper
import com.github.radlance.profile.presentation.ProfileCredentialMapper
import com.github.radlance.profile.presentation.ProfileCredentialUiState
import com.github.radlance.profile.presentation.ProfileMapperFacade
import com.github.radlance.profile.presentation.ProfileProviderMapper
import com.github.radlance.profile.presentation.ProfileProviderUi
import com.github.radlance.profile.presentation.ProfileUiState
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
    fun provideHandleProfile(handleProfile: BaseHandleProfile): HandleProfile

    @Binds
    fun provideProfileProviderMapper(profileProviderMapper: ProfileProviderMapper): com.github.radlance.api.service.ProfileProvider.Mapper<ProfileProviderUi>

    @Binds
    fun provideProfileMapperFacade(profileMapperFacade: BaseProfileMapperFacade): ProfileMapperFacade

    @Binds
    fun provideProfileCredentialMapper(profileCredentialMapper: ProfileCredentialMapper): CredentialResult.Mapper<ProfileCredentialUiState>

    @Binds
    fun provideDeleteProfileMapper(deleteProfileMapper: DeleteProfileMapper): UnitResult.Mapper<DeleteProfileUiState>

    @Binds
    fun provideHandleProfileRemoteDataSource(handleProfileRemoteDataSource: HandleProfileRemoteDataSource.Base): HandleProfileRemoteDataSource
}