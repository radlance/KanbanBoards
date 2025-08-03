package com.github.radlance.ticket.create.di

import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.core.presentation.TicketUiState
import com.github.radlance.ticket.create.data.RemoteCreateTicketRepository
import com.github.radlance.ticket.create.domain.CreateTicketRepository
import com.github.radlance.ticket.create.presentation.BaseCreateTicketMapperFacade
import com.github.radlance.ticket.create.presentation.BaseFormatTime
import com.github.radlance.ticket.create.presentation.BaseHandleCreateTicket
import com.github.radlance.ticket.create.presentation.BoardMembersResultMapper
import com.github.radlance.ticket.create.presentation.BoardMembersUiStateCreate
import com.github.radlance.ticket.create.presentation.CreateTicketMapper
import com.github.radlance.ticket.create.presentation.CreateTicketMapperFacade
import com.github.radlance.ticket.create.presentation.FormatTime
import com.github.radlance.ticket.create.presentation.HandleCreateTicket
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface TicketModule {

    @Binds
    fun provideTicketRepository(ticketRepository: RemoteCreateTicketRepository): CreateTicketRepository

    @Binds
    fun provideBoardMembersResultMapper(
        boardMembersResultMapper: BoardMembersResultMapper
    ): com.github.radlance.core.domain.BoardMembersResult.Mapper<BoardMembersUiStateCreate>

    @Binds
    fun provideHandleAddTicket(handleCreateTicket: BaseHandleCreateTicket): HandleCreateTicket

    @Binds
    fun provideCreateTicketMapper(
        createTicketMapper: CreateTicketMapper
    ): UnitResult.Mapper<TicketUiState>

    @Binds
    fun provideTicketMapperFacade(createTicketMapperFacade: BaseCreateTicketMapperFacade): CreateTicketMapperFacade

    @Binds
    fun provideFormatTime(formatTime: BaseFormatTime): FormatTime
}