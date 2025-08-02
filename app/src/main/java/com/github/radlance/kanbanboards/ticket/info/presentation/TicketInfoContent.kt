package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.github.radlance.board.core.domain.Ticket
import com.github.radlance.core.presentation.BaseColumn

@Composable
fun TicketInfoContent(
    ticket: Ticket,
    modifier: Modifier = Modifier
) {
    BaseColumn(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text(
            text = ticket.name,
            fontSize = 30.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        HorizontalDivider(thickness = 4.dp, color = Color(ticket.colorHex.toColorInt()))
        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.height(IntrinsicSize.Min)) {

            OutlinedTextField(
                value = ticket.assignedMemberName.ifEmpty {
                    stringResource(com.github.radlance.core.R.string.no_assigned_member)
                },
                onValueChange = {},
                label = { Text(text = stringResource(com.github.radlance.core.R.string.assignee)) },
                modifier = Modifier.fillMaxWidth()
            )

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0f)
            ) {}
        }

        Spacer(Modifier.height(16.dp))


        if (ticket.description.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(com.github.radlance.core.R.string.description),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {

                        append(ticket.description)
                    },
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}