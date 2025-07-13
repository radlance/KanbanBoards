package com.github.radlance.kanbanboards.auth.domain

interface AuthRepository {

    suspend fun signInWithToken(userIdToken: String): AuthResult

    suspend fun signInWithEmail(email: String, password: String): AuthResult
}