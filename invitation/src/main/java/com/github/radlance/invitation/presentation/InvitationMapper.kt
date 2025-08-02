package com.github.radlance.invitation.presentation

import com.github.radlance.invitation.domain.Invitation
import com.github.radlance.invitation.domain.InvitationResult
import javax.inject.Inject

class InvitationMapper @Inject constructor() : InvitationResult.Mapper<InvitationUiState> {

    override fun mapSuccess(invitations: List<Invitation>) = InvitationUiState.Success(invitations)

    override fun mapError(message: String) = InvitationUiState.Error(message)
}