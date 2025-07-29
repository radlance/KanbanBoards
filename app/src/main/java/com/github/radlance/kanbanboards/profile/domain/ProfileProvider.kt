package com.github.radlance.kanbanboards.profile.domain

interface ProfileProvider {

    fun<T: Any> map(mapper: Mapper<T>): T

    interface Mapper<T: Any> {

        fun mapEmail(): T

        fun mapGoogle(): T
    }

    object Email : ProfileProvider {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapEmail()
    }

    object Google : ProfileProvider {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapGoogle()
    }
}