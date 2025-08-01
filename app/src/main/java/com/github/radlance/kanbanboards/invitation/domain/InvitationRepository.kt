package com.github.radlance.kanbanboards.invitation.domain

import kotlinx.coroutines.flow.Flow

interface InvitationRepository {

    fun invitations(): Flow<InvitationResult>

    fun accept(boardId: String, invitationId: String)

    fun decline(invitationId: String)
}