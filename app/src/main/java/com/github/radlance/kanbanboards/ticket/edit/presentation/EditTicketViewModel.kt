package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.common.presentation.BaseTicketViewModel
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditTicketViewModel @Inject constructor(
    private val editTicketRepository: EditTicketRepository,
    private val editTicketMapperFacade: EditTicketMapperFacade,
    private val handleEditTicket: HandleEditTicket,
    runAsync: RunAsync
) : BaseTicketViewModel(handleEditTicket, runAsync) {

    val ticketInfoUiState = handleEditTicket.ticketInfoEditUiState

    val boardMembersUiState = handleEditTicket.boardMembersUiState

    override fun action(
        boardId: String,
        title: String,
        color: String,
        description: String,
        assigneeId: String
    ) {
        TODO("Not yet implemented")
    }

    override fun fetchBoardMembers(boardId: String, ownerId: String) {
        editTicketRepository.boardMembers(boardId, ownerId).map {
            editTicketMapperFacade.mapBoardMembersResult(it)
        }.onEach {
            handleEditTicket.saveBoardMembersUiState(it)
        }.launchInViewModel()
    }

    fun fetchTicket(ticketId: String) {
        editTicketRepository.ticket(ticketId).map {
            editTicketMapperFacade.mapTicketInfoResult(it)
        }.onEach {
            handleEditTicket.saveTicketInfoEditUiState(it)
        }.launchInViewModel()
    }
}