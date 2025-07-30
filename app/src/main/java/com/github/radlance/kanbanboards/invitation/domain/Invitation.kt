package com.github.radlance.kanbanboards.invitation.domain

data class Invitation(
    val id: String,
    val boardName: String,
    val boardId: String,
    val ownerEmail: String
)
