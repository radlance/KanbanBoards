package com.github.radlance.kanbanboards.board.create.domain

import com.github.radlance.kanbanboards.common.domain.User

interface SearchUsersResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(users: List<User>): T

        fun mapError(message: String): T
    }

    data class Success(private val users: List<User>) : SearchUsersResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(users)
    }

    data class Error(private val message: String) : SearchUsersResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}