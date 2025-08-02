package com.github.radlance.board.core.domain

interface TicketResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(tickets: List<Ticket>): T

        fun mapError(message: String): T
    }

    data class Success(private val tickets: List<Ticket>) : TicketResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(tickets)
    }

    data class Error(private val message: String) : TicketResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}