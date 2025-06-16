package com.github.radlance.kanbanboards.common.data

import com.github.radlance.kanbanboards.common.domain.DomainException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

interface HandleError {

    fun handle(e: Exception): DomainException

    class Base @Inject constructor() : HandleError {
        override fun handle(e: Exception): DomainException {
            return if (e is UnknownHostException || e is ConnectException) {
                DomainException.NoInternetException()
            } else DomainException.ServerUnavailableException(e.message ?: e.toString())
        }
    }
}