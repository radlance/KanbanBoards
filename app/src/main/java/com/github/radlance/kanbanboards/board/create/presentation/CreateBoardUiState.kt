package com.github.radlance.kanbanboards.board.create.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.board.core.domain.BoardInfo

interface CreateBoardUiState {

    @Composable
    fun Show(navigateToBoardScreen: (BoardInfo) -> Unit, createBoardActions: CreateBoardActions)

    val buttonEnabled: Boolean

    abstract class Abstract(override val buttonEnabled: Boolean = true) : CreateBoardUiState

    data class Success(private val boardInfo: BoardInfo) : Abstract() {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) = navigateToBoardScreen(boardInfo)
    }

    data class Error(private val message: String) : Abstract() {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) = ErrorMessage(message)
    }

    data class AlreadyExists(private val message: String) : Abstract() {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) = createBoardActions.setBoardNameErrorMessage(message)
    }

    object Loading : Abstract(buttonEnabled = false) {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) = CircularProgressIndicator()
    }

    object Initial : Abstract() {

        @Composable
        override fun Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) = Unit
    }
}