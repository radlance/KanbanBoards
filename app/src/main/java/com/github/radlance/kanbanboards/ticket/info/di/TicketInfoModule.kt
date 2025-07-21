package com.github.radlance.kanbanboards.ticket.info.di

import com.github.radlance.kanbanboards.ticket.info.data.RemoteTicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoRepository
import com.github.radlance.kanbanboards.ticket.info.domain.TicketInfoResult
import com.github.radlance.kanbanboards.ticket.info.presentation.HandleTicketInfo
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoFacade
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoResultMapper
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TicketInfoModule {

    @Binds
    fun provideTicketInfoRepository(remoteTicketInfoRepository: RemoteTicketInfoRepository): TicketInfoRepository

    @Binds
    fun provideTicketInfoResultMapper(ticketInfoResultMapper: TicketInfoResultMapper): TicketInfoResult.Mapper<TicketInfoUiState>

    @Binds
    fun provideHandleTicketInfo(handleTicketInfo: HandleTicketInfo.Base): HandleTicketInfo

    @Binds
    fun provideTicketInfoFacade(ticketInfoFacade: TicketInfoFacade.Base): TicketInfoFacade
}