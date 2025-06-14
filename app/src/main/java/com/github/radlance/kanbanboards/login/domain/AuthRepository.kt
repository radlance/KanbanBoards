package com.github.radlance.kanbanboards.login.domain

interface AuthRepository {

    suspend fun signIn(userIdToken: String): AuthResult
}