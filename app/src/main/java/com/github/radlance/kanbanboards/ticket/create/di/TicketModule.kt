package com.github.radlance.kanbanboards.ticket.create.di

import com.github.radlance.common.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.create.data.RemoteCreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersResultMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersUiStateCreate
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.create.presentation.FormatTime
import com.github.radlance.kanbanboards.ticket.create.presentation.HandleCreateTicket
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TicketModule {

    @Binds
    fun provideTicketRepository(ticketRepository: RemoteCreateTicketRepository): CreateTicketRepository

    @Binds
    fun provideBoardMembersResultMapper(
        boardMembersResultMapper: BoardMembersResultMapper
    ): com.github.radlance.common.domain.BoardMembersResult.Mapper<BoardMembersUiStateCreate>

    @Binds
    fun provideHandleAddTicket(handleCreateTicket: HandleCreateTicket.Base): HandleCreateTicket

    @Binds
    fun provideCreateTicketMapper(
        createTicketMapper: CreateTicketMapper
    ): UnitResult.Mapper<TicketUiState>

    @Binds
    fun provideTicketMapperFacade(createTicketMapperFacade: CreateTicketMapperFacade.Base): CreateTicketMapperFacade

    @Binds
    fun provideFormatTime(formatTime: FormatTime.Base): FormatTime
}