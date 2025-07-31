package com.github.radlance.kanbanboards.invitation.domain

import kotlinx.coroutines.flow.Flow

interface InvitationRepository {

    fun invitations(): Flow<InvitationResult>

    suspend fun accept(boardId: String, invitationId: String)

    suspend fun decline(invitationId: String)
}