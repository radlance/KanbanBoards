package com.github.radlance.kanbanboards.board.presentation

import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.board.domain.BoardRepository
import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val handleBoard: HandleBoard,
    private val facade: BoardMapperFacade,
    runAsync: RunAsync
) : BaseViewModel(runAsync), BoardTicketActions {

    val boardUiState = handleBoard.boardUiState

    override val ticketBoardUiState = handleBoard.ticketBoardUiState

    fun fetchBoard(boardInfo: BoardInfo) {
        boardRepository.board(boardInfo.id).map {
            facade.mapBoardResult(it)
        }.onStart { handleBoard.saveBoardUiState(BoardUiState.Success(boardInfo)) }.onEach {
            handleBoard.saveBoardUiState(it)
        }.launchInViewModel()
    }

    override fun fetchTickets(boardId: String) {
        boardRepository.tickets(boardId).map {
            facade.mapTicketResult(it)
        }.onEach {
            handleBoard.saveTicketUiState(it)
        }.launchInViewModel()
    }

    override fun moveTicket(ticketId: String, column: ColumnUi) {
        boardRepository.moveTicket(ticketId, facade.mapColumnUi(column))
    }
}

interface BoardTicketActions {

    val ticketBoardUiState: StateFlow<TicketBoardUiState>

    fun fetchTickets(boardId: String)

    fun moveTicket(ticketId: String, column: ColumnUi)
}