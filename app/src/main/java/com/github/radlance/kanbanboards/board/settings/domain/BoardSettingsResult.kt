package com.github.radlance.kanbanboards.board.settings.domain

import com.github.radlance.kanbanboards.board.core.domain.BoardInfo
import com.github.radlance.kanbanboards.common.domain.User

interface BoardSettingsResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(users: List<User>, members: List<User>, board: BoardInfo): T

        fun mapError(message: String): T
    }

    data class Success(
        private val users: List<User>,
        private val members: List<User>,
        private val board: BoardInfo
    ) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(
            users, members, board
        )
    }

    data class Error(private val message: String) : BoardSettingsResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}