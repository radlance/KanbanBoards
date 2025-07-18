package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.create.domain.NewTicket
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val ticketMapperFacade: TicketMapperFacade,
    private val handleTicket: HandleTicket,
    private val formatTime: FormatTime,
    runAsync: RunAsync
) : BaseViewModel(runAsync), TicketActions {

    val boardMembersUiState = handleTicket.boardMembersUiState

    override val createTicketUiState: StateFlow<CreateTicketUiState>
        get() = handleTicket.createTicketUiState

    fun fetchBoardMembers(boardId: String) {
        ticketRepository.boardMembers(boardId).map {
            ticketMapperFacade.mapBoardMembersResult(it)
        }.onEach {
            handleTicket.saveBoardMembersUiState(it)
        }.launchInViewModel()
    }

    override fun createTicket(
        boardId: String,
        title: String,
        color: String,
        description: String,
        assigneeId: String
    ) {

        handleTicket.saveCreateTicketUiState(CreateTicketUiState.Loading)

        val newTicket = NewTicket(
            boardId = boardId,
            colorHex = color,
            name = title,
            description = description,
            assignedMemberId = assigneeId,
            creationDate = formatTime.now()
        )

        handle(background = { ticketRepository.createTicket(newTicket) }) {
            handleTicket.saveCreateTicketUiState(ticketMapperFacade.mapCreateTicketResult(it))
        }
    }

    override fun clearCreateTicketUiState() {
        handleTicket.saveCreateTicketUiState(CreateTicketUiState.Initial)
    }

}

interface TicketActions {

    val createTicketUiState: StateFlow<CreateTicketUiState>

    fun createTicket(
        boardId: String,
        title: String,
        color: String,
        description: String,
        assigneeId: String
    )

    fun clearCreateTicketUiState()
}