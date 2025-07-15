package com.github.radlance.kanbanboards.ticket.create.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.ticket.create.data.RemoteTicketRepository
import com.github.radlance.kanbanboards.ticket.create.data.TicketRemoteDataSource
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersResultMapper
import com.github.radlance.kanbanboards.ticket.create.presentation.BoardMembersUiState
import com.github.radlance.kanbanboards.ticket.create.presentation.HandleTicket
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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
}

@Module
@InstallIn(ViewModelComponent::class)
class TicketViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideHandleTicket(savedStateHandle: SavedStateHandle): HandleTicket {
        return HandleTicket.Base(savedStateHandle)
    }
}
