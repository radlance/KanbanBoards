package com.github.radlance.kanbanboards.core.data

import com.github.radlance.kanbanboards.core.domain.DomainException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

interface HandleError {

    fun handle(e: Exception): Nothing
}

internal class BaseHandleError @Inject constructor() : HandleError {
    override fun handle(e: Exception): Nothing {
        throw if (e is UnknownHostException || e is ConnectException) {
            DomainException.NoInternetException()
        } else DomainException.ServerUnavailableException(e.message ?: e.toString())
    }
}