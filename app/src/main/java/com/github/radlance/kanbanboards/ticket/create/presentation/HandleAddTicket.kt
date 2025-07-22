package com.github.radlance.kanbanboards.ticket.create.presentation

import com.github.radlance.kanbanboards.ticket.common.presentation.BaseHandleTicket
import com.github.radlance.kanbanboards.ticket.common.presentation.HandleTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface HandleAddTicket : HandleTicket {

    val boardMembersUiState: StateFlow<BoardMembersUiStateCreate>

    fun saveBoardMembersUiState(boardMembersUiStateCreate: BoardMembersUiStateCreate)

    class Base @Inject constructor() : BaseHandleTicket(), HandleAddTicket {

        private val boardMembersUiStateMutable = MutableStateFlow<BoardMembersUiStateCreate>(
            BoardMembersUiStateCreate.Loading
        )

        override val boardMembersUiState: StateFlow<BoardMembersUiStateCreate>
            get() = boardMembersUiStateMutable.asStateFlow()

        override fun saveBoardMembersUiState(boardMembersUiStateCreate: BoardMembersUiStateCreate) {
            boardMembersUiStateMutable.value = boardMembersUiStateCreate
        }
    }
}