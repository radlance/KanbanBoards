package com.github.radlance.core.domain

abstract class DomainException : Exception() {

    class NoInternetException : DomainException()

    data class ServerUnavailableException(override val message: String) : DomainException()
}