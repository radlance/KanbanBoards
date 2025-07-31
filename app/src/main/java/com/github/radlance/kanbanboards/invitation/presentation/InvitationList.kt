package com.github.radlance.kanbanboards.invitation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun InvitationList(
    invitations: List<Invitation>,
    onAcceptClick: (boardId: String, invitationId: String) -> Unit,
    onDeclineClick: (invitationId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        items(items = invitations, key = { it.id }) { invitation ->
            InvitationItem(
                onAcceptClick = onAcceptClick,
                onDeclineClick = onDeclineClick,
                invitation = invitation,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InvitationListPreview() {
    KanbanBoardsTheme {
        InvitationList(
            invitations = List(10) {
                Invitation(
                    id = it.toString(),
                    boardName = "board $it",
                    boardId = "1234",
                    ownerEmail = "owner$it@gmail.com",
                    sendDate = ZonedDateTime.of(LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC"))
                )
            },
            onAcceptClick = { _, _ -> },
            onDeclineClick = { },
            modifier = Modifier.padding(10.dp)
        )
    }
}