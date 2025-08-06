package com.github.radlance.kanbanboards.profile.domain

interface LoadProfileResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(name: String, email: String): T

        fun mapError(message: String): T
    }

    data class Success(private val name: String, private val email: String) : LoadProfileResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(name, email)
    }

    data class Error(private val message: String) : LoadProfileResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}