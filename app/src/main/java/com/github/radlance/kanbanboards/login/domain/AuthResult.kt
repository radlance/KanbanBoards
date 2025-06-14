package com.github.radlance.kanbanboards.login.domain

interface AuthResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(): T

        fun mapError(message: String): T
    }

    object Success : AuthResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess()
    }

    data class Error(private val message: String) : AuthResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}