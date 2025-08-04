package com.github.radlance.kanbanboards.api.service

data class NewMyUser internal constructor(
    val id: String,
    val email: String,
    val displayName: String
)
