package com.github.radlance.kanbanboards.invitation.domain

import kotlinx.coroutines.flow.Flow

interface InvitationRepository {

    fun invitations(): Flow<InvitationResult>

    suspend fun acceptInvite(boardId: String, invitationId: String)

    suspend fun declineInvite(boardId: String, invitationId: String)
}