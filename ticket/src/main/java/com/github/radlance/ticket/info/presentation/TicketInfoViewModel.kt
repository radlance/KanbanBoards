package com.github.radlance.ticket.info.presentation

import com.github.radlance.board.core.domain.Ticket
import com.github.radlance.board.core.presentation.TicketUi
import com.github.radlance.core.presentation.BaseViewModel
import com.github.radlance.core.presentation.RunAsync
import com.github.radlance.ticket.info.domain.TicketInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.toJavaLocalDateTime
import javax.inject.Inject

@HiltViewModel
class TicketInfoViewModel @Inject constructor(
    private val ticketInfoRepository: TicketInfoRepository,
    private val facade: TicketInfoFacade,
    private val handleTicketInfo: HandleTicketInfo,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val ticketInfoUiState = handleTicketInfo.ticketInfoUiState

    fun fetchTicket(ticketUi: TicketUi) {
        ticketInfoRepository.ticket(ticketUi.id).map {
            facade.mapTicketInfoResult(it)
        }.onStart {
            val ticket = with(ticketUi) {
                Ticket(
                    id = id,
                    colorHex = colorHex,
                    name = name,
                    description = description,
                    assignedMemberName = assignedMemberName,
                    assignedMemberId = assignedMemberId,
                    column = facade.mapColumnUi(column),
                    creationDate = creationDate.toJavaLocalDateTime()
                )
            }

            handleTicketInfo.saveTicketInfoUiState(TicketInfoUiState.Success(ticket))
        }.onEach {
            handleTicketInfo.saveTicketInfoUiState(it)
        }.launchInViewModel()
    }
}