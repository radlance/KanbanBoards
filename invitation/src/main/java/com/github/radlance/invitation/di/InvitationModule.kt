package com.github.radlance.invitation.di

import com.github.radlance.invitation.data.InvitationRemoteDataSource
import com.github.radlance.invitation.data.RemoteInvitationRepository
import com.github.radlance.invitation.domain.InvitationRepository
import com.github.radlance.invitation.domain.InvitationResult
import com.github.radlance.invitation.presentation.InvitationMapper
import com.github.radlance.invitation.presentation.InvitationUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InvitationModule {

    @Binds
    fun provideInvitationRemoteDataSource(invitationRemoteDataSource: InvitationRemoteDataSource.Base): InvitationRemoteDataSource

    @Binds
    fun provideInvitationRepository(invitationRepository: RemoteInvitationRepository): InvitationRepository

    @Binds
    fun provideInvitationMapper(invitationMapper: InvitationMapper): InvitationResult.Mapper<InvitationUiState>
}