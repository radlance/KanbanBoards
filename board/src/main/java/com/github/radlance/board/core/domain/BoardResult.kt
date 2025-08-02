package com.github.radlance.board.core.domain

import com.github.radlance.core.domain.BoardInfo

interface BoardResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(boardInfo: BoardInfo): T

        fun mapError(message: String): T

        fun mapNotExists(): T
    }

    data class Success(private val boardInfo: BoardInfo) : BoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(boardInfo)
    }

    data class Error(private val message: String) : BoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    object NotExists : BoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNotExists()
    }
}