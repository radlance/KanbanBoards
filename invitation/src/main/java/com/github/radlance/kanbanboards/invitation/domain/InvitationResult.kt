package com.github.radlance.kanbanboards.invitation.domain

interface InvitationResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(invitations: List<Invitation>): T

        fun mapError(message: String): T
    }

    data class Success(private val invitations: List<Invitation>) : InvitationResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapSuccess(invitations)
    }

    data class Error(private val message: String) : InvitationResult {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }
}