package com.github.radlance.kanbanboards.ticket.create.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface HandleTicket {

    fun boardMembersUiState(): StateFlow<BoardMembersUiState>

    fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState)

    class Base @Inject constructor(private val savedStateHandle: SavedStateHandle) : HandleTicket {

        override fun boardMembersUiState(): StateFlow<BoardMembersUiState> {
            return savedStateHandle.getStateFlow(KEY_MEMBERS, BoardMembersUiState.Loading)
        }

        override fun saveBoardMembersUiState(boardMembersUiState: BoardMembersUiState) {
            savedStateHandle[KEY_MEMBERS] = boardMembersUiState
        }

        companion object {
            private const val KEY_MEMBERS = "members"
        }
    }
}