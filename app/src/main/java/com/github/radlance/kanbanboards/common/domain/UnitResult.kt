package com.github.radlance.kanbanboards.common.domain

interface UnitResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(): T

        fun mapError(message: String): T
    }

    object Success : UnitResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess()
    }

    data class Error(private val message: String) : UnitResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}