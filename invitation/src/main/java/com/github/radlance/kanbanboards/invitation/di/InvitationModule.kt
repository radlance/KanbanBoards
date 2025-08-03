package com.github.radlance.kanbanboards.invitation.di

import com.github.radlance.kanbanboards.invitation.data.InvitationRemoteDataSource
import com.github.radlance.kanbanboards.invitation.data.RemoteInvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import com.github.radlance.kanbanboards.invitation.presentation.InvitationMapper
import com.github.radlance.kanbanboards.invitation.presentation.InvitationUiState
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