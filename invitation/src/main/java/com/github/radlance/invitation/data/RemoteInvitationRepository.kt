package com.github.radlance.invitation.data

import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.data.IgnoreHandle
import com.github.radlance.invitation.domain.Invitation
import com.github.radlance.invitation.domain.InvitationRepository
import com.github.radlance.invitation.domain.InvitationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RemoteInvitationRepository @Inject constructor(
    private val remoteDataSource: InvitationRemoteDataSource,
    private val manageResource: ManageResource,
    private val ignoreHandle: IgnoreHandle
) : InvitationRepository {

    override fun invitations(): Flow<InvitationResult> {
        return remoteDataSource.invitations().map<List<Invitation>, InvitationResult> {
            InvitationResult.Success(it)
        }.catch { e ->
            emit(
                InvitationResult.Error(
                    e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
                )
            )
        }
    }

    override fun accept(boardId: String, invitationId: String) = ignoreHandle.handle {
        remoteDataSource.accept(boardId, invitationId)
    }

    override fun decline(invitationId: String) = ignoreHandle.handle {
        remoteDataSource.decline(invitationId)
    }
}