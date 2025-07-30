package com.github.radlance.kanbanboards.invitation.presentation

import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import javax.inject.Inject

class InvitationMapper @Inject constructor(): InvitationResult.Mapper<InvitationUiState> {

    override fun mapSuccess(invitations: List<Invitation>) = InvitationUiState.Success(invitations)

    override fun mapError(message: String) = InvitationUiState.Error(message)
}