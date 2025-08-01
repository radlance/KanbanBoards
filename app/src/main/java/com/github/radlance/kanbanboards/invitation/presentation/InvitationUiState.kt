package com.github.radlance.kanbanboards.invitation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.radlance.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.invitation.domain.Invitation

interface InvitationUiState : InvitationCount {

    @Composable
    fun Show(columnScope: ColumnScope, invitationAction: InvitationAction)

    abstract class Abstract(override val count: Int = 0) : InvitationUiState

    data class Success(
        private val invitations: List<Invitation>
    ) : Abstract(count = invitations.size) {

        @Composable
        override fun Show(columnScope: ColumnScope, invitationAction: InvitationAction) = with(columnScope) {

            if (invitations.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.there_are_no_new_invitations))
                }
            } else {
                InvitationList(
                    invitations = invitations,
                    onAcceptClick = invitationAction::accept,
                    onDeclineClick = invitationAction::decline,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    data class Error(private val message: String) : Abstract() {

        @Composable
        override fun Show(columnScope: ColumnScope, invitationAction: InvitationAction) = with(columnScope) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : Abstract() {

        @Composable
        override fun Show(columnScope: ColumnScope, invitationAction: InvitationAction) = with(columnScope) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

interface InvitationCount {

    val count: Int
}