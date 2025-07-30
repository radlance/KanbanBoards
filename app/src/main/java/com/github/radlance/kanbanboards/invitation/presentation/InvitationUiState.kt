package com.github.radlance.kanbanboards.invitation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.common.presentation.ErrorMessage
import com.github.radlance.kanbanboards.invitation.domain.Invitation

interface InvitationUiState : InvitationCount {

    @Composable
    fun Show(columnScope: ColumnScope)

    abstract class Abstract(override val count: Int = 0) : InvitationUiState

    data class Success(
        private val invitations: List<Invitation>
    ) : Abstract(count = invitations.size) {

        @Composable
        override fun Show(columnScope: ColumnScope) = with(columnScope) {
            InvitationList(
                invitations = invitations,
                onAcceptClick = {},
                onDeclineClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }

    data class Error(private val message: String) : Abstract() {

        @Composable
        override fun Show(columnScope: ColumnScope) = with(columnScope) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : Abstract() {

        @Composable
        override fun Show(columnScope: ColumnScope) = with(columnScope) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

interface InvitationCount {

    val count: Int
}