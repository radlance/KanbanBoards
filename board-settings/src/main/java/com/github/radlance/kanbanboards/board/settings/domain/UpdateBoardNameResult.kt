package com.github.radlance.kanbanboards.board.settings.domain

interface UpdateBoardNameResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(): T

        fun mapError(message: String): T

        fun mapAlreadyExists(message: String): T
    }

    object Success : UpdateBoardNameResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess()
    }

    data class Error(private val message: String) : UpdateBoardNameResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    data class AlreadyExists(private val message: String) : UpdateBoardNameResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapAlreadyExists(message)
    }
}