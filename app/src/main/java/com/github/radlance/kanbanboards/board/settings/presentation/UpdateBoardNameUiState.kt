package com.github.radlance.kanbanboards.board.settings.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.github.radlance.common.presentation.ErrorMessage

interface UpdateBoardNameUiState {

    @Composable
    fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction)

    val buttonEnabled: Boolean

    abstract class Abstract(override val buttonEnabled: Boolean = true) : UpdateBoardNameUiState

    object Success : Abstract() {

        @Composable
        override fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction) {
            navigateUp()
        }
    }

    data class Error(private val message: String) : Abstract() {

        @Composable
        override fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction) {
            ErrorMessage(message)
        }
    }

    data class AlreadyExists(private val message: String) : Abstract() {

        @Composable
        override fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction) {
            updateBoardNameAction.setBoardNameErrorMessage(message)
        }
    }

    object Loading : Abstract(buttonEnabled = false) {

        @Composable
        override fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction) {
            CircularProgressIndicator()
        }
    }

    object Initial : Abstract() {

        @Composable
        override fun Show(navigateUp: () -> Unit, updateBoardNameAction: UpdateBoardNameAction) =
            Unit
    }
}