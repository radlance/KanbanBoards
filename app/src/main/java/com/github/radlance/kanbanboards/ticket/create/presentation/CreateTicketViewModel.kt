package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.board.core.domain.Column
import com.github.radlance.board.core.domain.NewTicket
import com.github.radlance.core.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.common.presentation.BaseTicketViewModel
import com.github.radlance.kanbanboards.ticket.common.presentation.TicketUiState
import com.github.radlance.kanbanboards.ticket.create.domain.CreateTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val createTicketRepository: CreateTicketRepository,
    private val createTicketMapperFacade: CreateTicketMapperFacade,
    private val handleTicket: HandleCreateTicket,
    private val formatTime: FormatTime,
    runAsync: RunAsync
) : BaseTicketViewModel(handleTicket, runAsync) {

    val boardMembersUiState = handleTicket.boardMembersUiState

    override fun fetchBoardMembers(boardId: String, ownerId: String) {
        createTicketRepository.boardMembers(boardId, ownerId).map {
            createTicketMapperFacade.mapBoardMembersResult(it)
        }.onEach {
            handleTicket.saveBoardMembersUiState(it)
        }.launchInViewModel()
    }

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

        handleTicket.saveTicketUiState(TicketUiState.Loading)

        val newTicket = NewTicket(
            boardId = boardId,
            colorHex = color,
            name = title,
            description = description,
            assignedMemberId = assigneeId,
            creationDate = formatTime.now()
        )

        handle(background = { createTicketRepository.createTicket(newTicket) }) {
            handleTicket.saveTicketUiState(createTicketMapperFacade.mapTicketResult(it))
        }
    }
}