package com.github.radlance.invitation.domain

import java.time.ZonedDateTime

data class Invitation(
    val id: String,
    val boardName: String,
    val boardId: String,
    val ownerEmail: String,
    val sendDate: ZonedDateTime
)