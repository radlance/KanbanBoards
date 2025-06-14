package com.github.radlance.kanbanboards.common.domain

abstract class DomainException : Exception() {

    class NoInternetException : DomainException()

    data class ServerUnavailableException(override val message: String) : DomainException()
}