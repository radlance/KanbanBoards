package com.github.radlance.kanbanboards.createboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import java.io.Serializable

interface CreateBoardUiState : Serializable {

    @Composable
    fun ColumnScope.Show(
        navigateToBoardScreen: (BoardInfo) -> Unit,
        createBoardActions: CreateBoardActions
    )

    abstract class Abstract(
        private val enabled: Boolean,
        private val loading: Boolean = false,
        private val fieldErrorMessage: String = "",
        private val createErrorMessage: String = ""
    ) : CreateBoardUiState {

        @Composable
        override fun ColumnScope.Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) {
            var boardNameFieldValue by rememberSaveable { mutableStateOf("") }

            val label = fieldErrorMessage.ifEmpty {
                stringResource(R.string.board_name)
            }

            OutlinedTextField(
                value = boardNameFieldValue,
                onValueChange = {
                    boardNameFieldValue = it
                    createBoardActions.checkBoard(it)
                },
                singleLine = true,
                isError = fieldErrorMessage.isNotEmpty(),
                placeholder = { Text(text = stringResource(R.string.at_least_3_symbol)) },
                label = { Text(text = label) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            if (createErrorMessage.isNotEmpty()) {
                Text(
                    text = createErrorMessage,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
                Spacer(Modifier.weight(1f))
            }


            Box(modifier = Modifier.safeDrawingPadding()) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { createBoardActions.createBoard(boardNameFieldValue) },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.create))
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }

    data class Success(private val boardInfo: BoardInfo) : CreateBoardUiState {

        @Composable
        override fun ColumnScope.Show(
            navigateToBoardScreen: (BoardInfo) -> Unit,
            createBoardActions: CreateBoardActions
        ) {
            navigateToBoardScreen(boardInfo)
        }
    }

    data class AlreadyExists(private val message: String) : Abstract(
        enabled = true, fieldErrorMessage = message
    )

    object CanCreate : Abstract(enabled = true) {

        private fun readResolve(): Any = CanCreate
    }

    object CanNotCreate : Abstract(enabled = false) {

        private fun readResolve(): Any = CanNotCreate
    }

    object Loading : Abstract(enabled = false, loading = true) {

        private fun readResolve(): Any = Loading
    }

    data class Error(private val message: String) : Abstract(
        enabled = true, createErrorMessage = message
    )
}