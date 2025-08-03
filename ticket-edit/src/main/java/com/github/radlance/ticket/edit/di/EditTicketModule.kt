package com.github.radlance.ticket.edit.di

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.UnitResult
import com.github.radlance.ticket.edit.data.HandleUnitResult
import com.github.radlance.ticket.edit.data.RemoteEditTicketRepository
import com.github.radlance.ticket.edit.domain.EditTicketRepository
import com.github.radlance.ticket.edit.presentation.BaseEditTicketMapperFacade
import com.github.radlance.ticket.edit.presentation.BaseHandleEditTicket
import com.github.radlance.ticket.edit.presentation.BoardMembersEditMapper
import com.github.radlance.ticket.edit.presentation.BoardMembersUiStateEdit
import com.github.radlance.ticket.edit.presentation.DeleteTicketMapper
import com.github.radlance.ticket.edit.presentation.DeleteTicketUiState
import com.github.radlance.ticket.edit.presentation.EditTicketMapperFacade
import com.github.radlance.ticket.edit.presentation.HandleEditTicket
import com.github.radlance.ticket.edit.presentation.TicketInfoEditMapper
import com.github.radlance.ticket.edit.presentation.TicketInfoEditUiState
import com.github.radlance.ticket.info.domain.TicketInfoResult
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