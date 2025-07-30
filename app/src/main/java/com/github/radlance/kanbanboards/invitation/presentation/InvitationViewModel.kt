package com.github.radlance.kanbanboards.invitation.presentation

import com.github.radlance.kanbanboards.common.presentation.BaseViewModel
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.invitation.domain.InvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val invitationRepository: InvitationRepository,
    private val mapper: InvitationResult.Mapper<InvitationUiState>,
    runAsync: RunAsync
) : BaseViewModel(runAsync), InvitationCountAction, InvitationAction {

    val invitationsUiState = invitationRepository.invitations().map {
        it.map(mapper)
    }.stateInViewModel(initialValue = InvitationUiState.Loading)

    override val invitationCount: StateFlow<InvitationCount> = invitationsUiState

    override fun acceptInvite(boardId: String, invitationId: String) {
        handle(background = { invitationRepository.acceptInvite(boardId, invitationId) }, ui = {})
    }

    override fun declineInvite(boardId: String, invitationId: String) {
        handle(background = { invitationRepository.declineInvite(boardId, invitationId) }, ui = {})
    }
}

interface InvitationAction {

    fun acceptInvite(boardId: String, invitationId: String)

    fun declineInvite(boardId: String, invitationId: String)
}

interface InvitationCountAction {

    val invitationCount: StateFlow<InvitationCount>
}