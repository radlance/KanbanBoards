package com.github.radlance.kanbanboards.invitation.domain

data class Invitation(
    val boardName: String,
    val boardId: String,
    val ownerEmail: String
)
