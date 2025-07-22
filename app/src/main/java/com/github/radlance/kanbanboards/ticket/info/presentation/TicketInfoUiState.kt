package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.board.domain.Ticket
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage

interface TicketInfoUiState {

    fun <T: Any> map(mapper: Mapper<T>): T

    interface Mapper<T: Any> {

        fun mapSuccess(ticket: Ticket): T

        fun mapError(message: String): T

        fun mapLoading(): T
    }

    @Composable
    fun Show(modifier: Modifier = Modifier)

    data class Success(private val ticket: Ticket) : TicketInfoUiState {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(ticket)

        @Composable
        override fun Show(modifier: Modifier) {

            TicketInfoContent(ticket = ticket, modifier = modifier)
        }
    }

    data class Error(private val message: String) : TicketInfoUiState {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)

        @Composable
        override fun Show(modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : TicketInfoUiState {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapLoading()

        @Composable
        override fun Show(modifier: Modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}