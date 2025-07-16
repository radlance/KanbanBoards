package com.github.radlance.kanbanboards.ticket.create.di

import com.github.radlance.kanbanboards.ticket.create.data.RemoteTicketRepository
import com.github.radlance.kanbanboards.ticket.create.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersResultMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersUiState
import com.github.radlance.kanbanboards.ticket.create.presentation.HandleTicket
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TicketModule {

    @Binds
    fun provideTicketRemoteDataSource(
        ticketRemoteDataSource: TicketRemoteDataSource.Base
    ): TicketRemoteDataSource

    @Binds
    fun provideTicketRepository(ticketRepository: RemoteTicketRepository): TicketRepository

    @Binds
    fun provideBoardMembersResultMapper(
        boardMembersResultMapper: BoardMembersResultMapper
    ): BoardMembersResult.Mapper<BoardMembersUiState>

    @Binds
    fun provideHandleTicket(handleTicket: HandleTicket.Base): HandleTicket
}