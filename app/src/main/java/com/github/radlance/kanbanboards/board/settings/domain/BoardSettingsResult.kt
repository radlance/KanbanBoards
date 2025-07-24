package com.github.radlance.kanbanboards.board.settings.domain

import com.github.radlance.kanbanboards.common.domain.User

interface BoardSettingsResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(users: List<User>, members: List<BoardMember>): T

        fun mapError(message: String): T
    }

    data class Success(
        private val users: List<User>,
        private val members: List<BoardMember>
    ) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(users, members)
    }

    data class Error(private val message: String) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}