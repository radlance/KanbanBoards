package com.github.radlance.kanbanboards.ticket.create.di

import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.create.data.RemoteTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersResultMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersUiState
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketUiState
import com.github.radlance.kanbanboards.ticket.create.presentation.FormatTime
import com.github.radlance.kanbanboards.ticket.create.presentation.HandleTicket
import com.github.radlance.kanbanboards.ticket.create.presentation.TicketMapperFacade
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TicketModule {

    @Binds
    fun provideTicketRepository(ticketRepository: RemoteTicketRepository): TicketRepository

    @Binds
    fun provideBoardMembersResultMapper(
        boardMembersResultMapper: BoardMembersResultMapper
    ): BoardMembersResult.Mapper<BoardMembersUiState>

    @Binds
    fun provideHandleTicket(handleTicket: HandleTicket.Base): HandleTicket

    @Binds
    fun provideCreateTicketMapper(
        createTicketMapper: CreateTicketMapper
    ): UnitResult.Mapper<CreateTicketUiState>

    @Binds
    fun provideTicketMapperFacade(ticketMapperFacade: TicketMapperFacade.Base): TicketMapperFacade

    @Binds
    fun provideFormatTime(formatTime: FormatTime.Base): FormatTime
}