package com.github.radlance.kanbanboards.boards.domain

interface BoardsResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(boards: List<Board>): T

        fun mapError(message: String): T
    }

    data class Success(private val boards: List<Board>) : BoardsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(boards)
    }

    data class Error(private val message: String) : BoardsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}