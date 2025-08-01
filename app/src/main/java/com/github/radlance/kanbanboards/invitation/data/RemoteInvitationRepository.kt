package com.github.radlance.kanbanboards.invitation.data

import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.data.IgnoreHandle
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.invitation.domain.InvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteInvitationRepository @Inject constructor(
    private val remoteDataSource: InvitationRemoteDataSource,
    private val manageResource: ManageResource,
    private val ignoreHandle: IgnoreHandle
) : InvitationRepository {

    override fun invitations(): Flow<InvitationResult> {
        return remoteDataSource.invitations().map<List<Invitation>, InvitationResult> {
            InvitationResult.Success(it)
        }.catch { e ->
            emit(InvitationResult.Error(e.message ?: manageResource.string(R.string.error)))
        }
    }

    override fun accept(boardId: String, invitationId: String) = ignoreHandle.handle {
        remoteDataSource.accept(boardId, invitationId)
    }

    override fun decline(invitationId: String) = ignoreHandle.handle {
        remoteDataSource.decline(invitationId)
    }
}