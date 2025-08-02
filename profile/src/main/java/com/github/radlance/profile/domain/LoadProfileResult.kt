package com.github.radlance.profile.domain

interface LoadProfileResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun map(name: String, email: String): T
    }

    data class Base(private val name: String, private val email: String) : LoadProfileResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.map(name, email)
    }
}