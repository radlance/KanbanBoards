package com.github.radlance.kanbanboards.ticket.create.domain

interface BoardMembersResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(members: List<BoardMember>): T

        fun mapError(message: String): T
    }

    data class Success(private val members: List<BoardMember>) : BoardMembersResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(members)
    }

    data class Error(private val message: String) : BoardMembersResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}