package com.github.radlance.kanbanboards.ticket.edit.di

import com.github.radlance.kanbanboards.core.domain.BoardMembersResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.ticket.edit.data.HandleUnitResult
import com.github.radlance.kanbanboards.ticket.edit.data.RemoteEditTicketRepository
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import com.github.radlance.kanbanboards.ticket.edit.presentation.BaseEditTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.edit.presentation.BaseHandleEditTicket
import com.github.radlance.kanbanboards.ticket.edit.presentation.BoardMembersEditMapper
import com.github.radlance.kanbanboards.ticket.edit.presentation.BoardMembersUiStateEdit
import com.github.radlance.kanbanboards.ticket.edit.presentation.DeleteTicketMapper
import com.github.radlance.kanbanboards.ticket.edit.presentation.DeleteTicketUiState
import com.github.radlance.kanbanboards.ticket.edit.presentation.EditTicketMapperFacade
import com.github.radlance.kanbanboards.ticket.edit.presentation.HandleEditTicket
import com.github.radlance.kanbanboards.ticket.edit.presentation.TicketInfoEditMapper
import com.github.radlance.kanbanboards.ticket.edit.presentation.TicketInfoEditUiState
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EditTicketModule {

    @Binds
    fun provideEditTicketRepository(remoteTicketRepository: RemoteEditTicketRepository): EditTicketRepository

    @Binds
    fun provideEditTicketMapperFacade(editTicketMapperFacade: BaseEditTicketMapperFacade): EditTicketMapperFacade

    @Binds
    fun provideBoardMembersEditMapper(boardMembersEditMapper: BoardMembersEditMapper): BoardMembersResult.Mapper<BoardMembersUiStateEdit>

    @Binds
    fun provideTicketInfoEditMapper(ticketInfoEditMapper: TicketInfoEditMapper): TicketInfoResult.Mapper<TicketInfoEditUiState>

    @Binds
    fun provideHandleEditTicket(handleEditTicket: BaseHandleEditTicket): HandleEditTicket

    @Binds
    fun provideHandleUnitResult(handleUnitResult: HandleUnitResult.Base): HandleUnitResult

    @Binds
    fun provideDeleteTicketMapper(deleteTicketMapper: DeleteTicketMapper): UnitResult.Mapper<DeleteTicketUiState>
}