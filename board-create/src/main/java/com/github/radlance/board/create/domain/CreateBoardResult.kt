package com.github.radlance.board.create.domain

import com.github.radlance.core.domain.BoardInfo

interface CreateBoardResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(boardInfo: BoardInfo): T

        fun mapError(message: String): T

        fun mapAlreadyExists(message: String): T
    }

    data class Success(private val boardInfo: BoardInfo) : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(boardInfo)
    }

    data class Error(private val message: String) : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    data class AlreadyExists(private val message: String) : CreateBoardResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapAlreadyExists(message)
    }
}