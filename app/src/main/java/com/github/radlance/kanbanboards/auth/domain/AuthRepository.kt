package com.github.radlance.kanbanboards.auth.domain

interface AuthRepository {

    suspend fun signIn(userIdToken: String): AuthResult
}