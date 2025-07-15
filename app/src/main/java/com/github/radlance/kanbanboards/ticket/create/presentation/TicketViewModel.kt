package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import com.github.radlance.kanbanboards.ticket.create.domain.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val mapper: BoardMembersResult.Mapper<BoardMembersUiState>,
    private val handleTicket: HandleTicket,
    runAsync: RunAsync
) : BaseViewModel(runAsync) {

    val boardMembersUiState = handleTicket.boardMembersUiState()

    fun fetchBoardMembers(boardId: String) {
        ticketRepository.boardMembers(boardId).map {
            it.map(mapper)
        }.onEach {
            handleTicket.saveBoardMembersUiState(it)
        }.launchInViewModel()
    }
}