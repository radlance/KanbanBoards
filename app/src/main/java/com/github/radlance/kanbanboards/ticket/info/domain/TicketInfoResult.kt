package com.github.radlance.kanbanboards.ticket.info.domain

import com.github.radlance.board.core.domain.Ticket

interface TicketInfoResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(ticket: Ticket): T

        fun mapError(message: String): T

        fun mapNotExists(): T
    }

    data class Success(private val ticket: Ticket) : TicketInfoResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(ticket)
    }

    data class Error(private val message: String) : TicketInfoResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    object NotExists : TicketInfoResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNotExists()
    }
}