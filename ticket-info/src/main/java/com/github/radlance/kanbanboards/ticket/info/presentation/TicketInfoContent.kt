package com.github.radlance.kanbanboards.ticket.info.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.github.radlance.kanbanboards.board.core.domain.Ticket
import com.github.radlance.kanbanboards.core.presentation.BaseColumn

@Composable
internal fun TicketInfoContent(
    ticket: Ticket,
    modifier: Modifier = Modifier
) {
    BaseColumn(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text(
            text = ticket.name,
            fontSize = 30.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        HorizontalDivider(thickness = 4.dp, color = Color(ticket.colorHex.toColorInt()))
        Spacer(Modifier.height(16.dp))

        if (ticket.assignedMemberNames.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(com.github.radlance.core.R.string.assigned_to),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ticket.assignedMemberNames.forEach {
                        Text(text = it, maxLines = 1, overflow = TextOverflow.Ellipsis)

                        if (it != ticket.assignedMemberNames.last()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        if (ticket.description.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(com.github.radlance.core.R.string.description),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
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