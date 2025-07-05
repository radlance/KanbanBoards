package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.radlance.kanbanboards.board.domain.BoardInfo
import com.github.radlance.kanbanboards.common.presentation.BaseColumn
import java.io.Serializable

interface BoardUiState : Serializable {

    @Composable
    fun Show(modifier: Modifier)

    data class Success(private val boardInfo: BoardInfo) : BoardUiState {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Show(modifier: Modifier) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = boardInfo.name)
                        }
                    )
                },
                modifier = modifier
            ) { contentPadding ->
                BaseColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(contentPadding)
                ) {

                }
            }
        }
    }

    data class Error(private val message: String) : BoardUiState {

        @Composable
        override fun Show(modifier: Modifier) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }

    object Loading : BoardUiState {

        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(modifier: Modifier) {
            CircularProgressIndicator()
        }
    }
}