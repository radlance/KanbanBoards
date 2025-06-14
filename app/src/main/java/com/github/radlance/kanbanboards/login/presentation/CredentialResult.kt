package com.github.radlance.kanbanboards.login.presentation

interface CredentialResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(idToken: String): T

        fun mapError(): T
    }

    data class Success(private val idToken: String) : CredentialResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(idToken)
    }

    object Error : CredentialResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError()
    }
}