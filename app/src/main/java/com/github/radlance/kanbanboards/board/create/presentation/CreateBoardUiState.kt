package com.github.radlance.kanbanboards.board.create.presentation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo

interface CreateBoardUiState {

    @Composable
    fun Show(
        navigateToBoardScreen: (BoardInfo) -> Unit,
        createBoardActions: CreateBoardActions,
        columnScope: ColumnScope
    )

    abstract class Abstract(
        private val enabled: Boolean,
        private val loading: Boolean = false,
        private val nameFieldErrorMessage: String = "",
        private val createErrorMessage: String = ""
    ) : CreateBoardUiState {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions,
            columnScope: ColumnScope
        ) = CreateBoardContent(
            columnScope = columnScope,
            enabled = enabled,
            loading = loading,
            nameFieldErrorMessage = nameFieldErrorMessage,
            createErrorMessage = createErrorMessage,
            createBoardActions = createBoardActions
        )
    }

    data class Success(private val boardInfo: BoardInfo) : Abstract(
        enabled = true
    ) {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions,
            columnScope: ColumnScope
        ) {
            super.Show(navigateToBoardScreen, createBoardActions, columnScope)
            navigateToBoardScreen(boardInfo)
        }
    }

    data class AlreadyExists(private val message: String) : Abstract(
        enabled = true, nameFieldErrorMessage = message
    )

    object CanCreate : Abstract(enabled = true)

    object CanNotCreate : Abstract(enabled = false)

    object Loading : Abstract(enabled = false, loading = true)

    data class Error(private val message: String) : Abstract(
        enabled = true, createErrorMessage = message
    )
}