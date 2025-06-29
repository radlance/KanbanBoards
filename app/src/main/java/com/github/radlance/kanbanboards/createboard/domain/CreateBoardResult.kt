package com.github.radlance.kanbanboards.createboard.domain

interface CreateBoardResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(): T

        fun mapError(message: String): T

        fun mapAlreadyExists(message: String): T
    }

    object Success : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess()
    }

    data class Error(private val message: String) : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    data class AlreadyExists(private val message: String) : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapAlreadyExists(message)
    }
}