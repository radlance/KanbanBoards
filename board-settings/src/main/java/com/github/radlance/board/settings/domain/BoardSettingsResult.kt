package com.github.radlance.board.settings.domain

import com.github.radlance.core.domain.User

interface BoardSettingsResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(users: List<User>, members: List<BoardUser>, invited: List<BoardUser>): T

        fun mapError(message: String): T
    }

    data class Success(
        private val users: List<User>,
        private val members: List<BoardUser>,
        private val invited: List<BoardUser>
    ) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>) = mapper.mapSuccess(users, members, invited)
    }

    data class Error(private val message: String) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>) = mapper.mapError(message)
    }
}