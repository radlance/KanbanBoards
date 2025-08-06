package com.github.radlance.kanbanboars.profile.edit.di

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboars.profile.edit.data.EditProfileRemoteDataSource
import com.github.radlance.kanbanboars.profile.edit.data.HandleEditProfileRemoteDataSource
import com.github.radlance.kanbanboars.profile.edit.data.RemoteEditProfileRepository
import com.github.radlance.kanbanboars.profile.edit.domain.EditProfileRepository
import com.github.radlance.kanbanboars.profile.edit.presentation.BaseEditProfileMapperFacade
import com.github.radlance.kanbanboars.profile.edit.presentation.BaseHandleEditProfile
import com.github.radlance.kanbanboars.profile.edit.presentation.DeleteProfileMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.DeleteProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileMapperFacade
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.HandleEditProfile
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileCredentialMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileCredentialUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileEditMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileEditUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileProviderMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileProviderUi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EditProfileModule {

    @Binds
    fun provideEditProfileRemoteDataSource(editProfileRemoteDataSource: EditProfileRemoteDataSource.Base): EditProfileRemoteDataSource

    @Binds
    fun provideEditProfileRepository(editProfileRepository: RemoteEditProfileRepository): EditProfileRepository

    @Binds
    fun provideProfileEditMapper(profileEditMapper: ProfileEditMapper): LoadProfileResult.Mapper<ProfileEditUiState>

    @Binds
    fun provideEditProfileMapper(editProfileMapper: EditProfileMapper): UnitResult.Mapper<EditProfileUiState>

    @Binds
    fun provideEditProfileMapperFacade(editProfileMapperFacade: BaseEditProfileMapperFacade): EditProfileMapperFacade

    @Binds
    fun provideHandleEditProfile(handleEditProfile: BaseHandleEditProfile): HandleEditProfile

    @Binds
    fun provideProfileProviderMapper(profileProviderMapper: ProfileProviderMapper): ProfileProvider.Mapper<ProfileProviderUi>

    @Binds
    fun provideProfileCredentialMapper(profileCredentialMapper: ProfileCredentialMapper): CredentialResult.Mapper<ProfileCredentialUiState>

    @Binds
    fun provideDeleteProfileMapper(deleteProfileMapper: DeleteProfileMapper): UnitResult.Mapper<DeleteProfileUiState>

    @Binds
    fun provideHandleProfileRemoteDataSource(handleProfileRemoteDataSource: HandleEditProfileRemoteDataSource.Base): HandleEditProfileRemoteDataSource
}