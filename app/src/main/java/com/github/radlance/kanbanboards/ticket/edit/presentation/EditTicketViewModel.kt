package com.github.radlance.kanbanboards.ticket.edit.presentation

import com.github.radlance.common.presentation.RunAsync
import com.github.radlance.kanbanboards.board.core.domain.Column
import com.github.radlance.kanbanboards.ticket.common.presentation.BaseTicketViewModel
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicket
import com.github.radlance.kanbanboards.ticket.edit.domain.EditTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
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

    val deleteTicketUiState = handleEditTicket.deleteTicketUiState

    override fun action(
        ticketId: String,
        boardId: String,
        column: Column,
        title: String,
        color: String,
        description: String,
        assigneeId: String,
        creationDate: LocalDateTime
    ) {
        handleEditTicket.saveTicketUiState(TicketUiState.Loading)

        val newTicket = EditTicket(
            colorHex = color,
            name = title,
            description = description,
            assignedMemberId = assigneeId,
            id = ticketId,
            boardId = boardId,
            column = column,
            creationDate = creationDate
        )

        handle(background = { editTicketRepository.editTicket(newTicket) }) {
            handleEditTicket.saveTicketUiState(editTicketMapperFacade.mapTicketResult(it))
        }
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

    fun deleteTicket(ticketId: String) {
        handle(background = { editTicketRepository.deleteTicket(ticketId) }) {
            handleEditTicket.saveDeleteTicketUiState(editTicketMapperFacade.mapDeleteTicket(it))
        }
    }

    fun clearDeleteTicketUiState() {
        handleEditTicket.saveDeleteTicketUiState(DeleteTicketUiState.Initial)
    }
}