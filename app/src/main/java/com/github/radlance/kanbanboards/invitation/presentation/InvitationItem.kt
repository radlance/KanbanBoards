package com.github.radlance.kanbanboards.invitation.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme

@Composable
fun InvitationItem(
    onAcceptClick: (boardId: String, invitationId: String) -> Unit,
    onDeclineClick: (boardId: String, invitationId: String) -> Unit,
    invitation: Invitation,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = ButtonDefaults.outlinedButtonBorder().brush,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(invitation.ownerEmail)
                    }
                    append(stringResource(R.string.invites_you_on, invitation.boardName))
                }
            )
            Spacer(Modifier.height(16.dp))
            Row {
                Button(
                    onClick = { onAcceptClick(invitation.boardId, invitation.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.accept))
                }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { onDeclineClick(invitation.boardId, invitation.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.decline))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InvitationItemPreview() {

    KanbanBoardsTheme {
        InvitationItem(
            invitation = Invitation(
                id = "123456",
                boardName = "board name",
                boardId = "123456",
                ownerEmail = "test@gmail.com"
            ),
            onAcceptClick = { _, _ -> },
            onDeclineClick = { _, _ -> },
            modifier = Modifier.padding(10.dp)
        )
    }
}