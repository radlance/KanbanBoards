package com.github.radlance.invitation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeAgoText(
    dateTime: ZonedDateTime,
    modifier: Modifier = Modifier,
    content: @Composable (String) -> Unit
) {
    var currentTime by remember { mutableStateOf(ZonedDateTime.now()) }

    LaunchedEffect(dateTime) {
        var nextUpdateTime = dateTime.plusMinutes(1)

        while (true) {
            val now = ZonedDateTime.now()
            currentTime = now

            if (now >= nextUpdateTime) {
                nextUpdateTime = nextUpdateTime.plusMinutes(1)
            }

            val delayMillis = Duration.between(now, nextUpdateTime).toMillis()

            if (delayMillis > 0) {
                delay(delayMillis)
            } else {
                delay(1000)
            }
        }
    }

    val duration = Duration.between(dateTime, currentTime)

    val text = when {
        duration.toMinutes() < 1 -> "Now"
        duration.toHours() < 1 -> "${duration.toMinutes()} minute${if (duration.toMinutes() != 1L) "s" else ""} ago"
        duration.toDays() < 1 -> "${duration.toHours()} hour${if (duration.toHours() != 1L) "s" else ""} ago"
        isYesterday(
            dateTime,
            currentTime
        ) -> "Yesterday, ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"

        else -> dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    }

    Box(modifier = modifier) {
        content(text)
    }
}

private fun isYesterday(dateTime: ZonedDateTime, currentTime: ZonedDateTime): Boolean {
    val yesterday = currentTime.minusDays(1)
    return dateTime.toLocalDate() == yesterday.toLocalDate()
}