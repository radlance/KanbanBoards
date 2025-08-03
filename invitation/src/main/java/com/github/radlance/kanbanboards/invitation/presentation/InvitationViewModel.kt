package com.github.radlance.kanbanboards.invitation.presentation

import com.github.radlance.kanbanboards.core.presentation.BaseViewModel
import com.github.radlance.kanbanboards.core.presentation.RunAsync
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

    val invitationsUiState
        get() = invitationRepository.invitations().map {
            it.map(mapper)
        }.stateInViewModel(initialValue = InvitationUiState.Loading)

    override val invitationCount: StateFlow<InvitationCount> get() = invitationsUiState

    override fun accept(boardId: String, invitationId: String) {
        invitationRepository.accept(boardId, invitationId)
    }

    override fun decline(invitationId: String) {
        invitationRepository.decline(invitationId)
    }
}

interface InvitationAction {

    fun accept(boardId: String, invitationId: String)

    fun decline(invitationId: String)
}

interface InvitationCountAction {

    val invitationCount: StateFlow<InvitationCount>
}