package com.github.radlance.kanbanboards.core.domain

abstract class DomainException : Exception() {

    class NoInternetException : DomainException()

    data class ServerUnavailableException(override val message: String) : DomainException()
}